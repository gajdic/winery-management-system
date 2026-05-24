param(
    [Parameter(Mandatory = $true)]
    [string] $AccessToken,

    [string] $HarvestBaseUrl = "http://localhost:8081",
    [string] $ProductionBaseUrl = "http://localhost:8082/api",
    [string] $InventoryBaseUrl = "http://localhost:8083/inventory-service/api",
    [string] $CustomerBaseUrl = "http://localhost:8084",

    [string] $BatchId = "cccccccc-cccc-4ccc-8ccc-cccccccccccc",
    [string] $WineId = "33333333-3333-4333-8333-333333333333"
)

$ErrorActionPreference = "Stop"

$headers = @{
    Authorization = "Bearer $AccessToken"
    Accept = "application/json"
}

function Write-Step {
    param([string] $Message)
    Write-Host ""
    Write-Host "== $Message =="
}

function Invoke-Api {
    param(
        [Parameter(Mandatory = $true)]
        [ValidateSet("GET", "POST", "PUT", "PATCH", "DELETE")]
        [string] $Method,

        [Parameter(Mandatory = $true)]
        [string] $Uri,

        [object] $Body = $null,
        [switch] $AllowNotFound
    )

    try {
        $args = @{
            Method = $Method
            Uri = $Uri
            Headers = $headers
        }

        if ($null -ne $Body) {
            $args.ContentType = "application/json"
            $args.Body = ($Body | ConvertTo-Json -Depth 20)
        }

        return Invoke-RestMethod @args
    } catch {
        $statusCode = $null
        if ($_.Exception.Response -and $_.Exception.Response.StatusCode) {
            $statusCode = [int]$_.Exception.Response.StatusCode
        }

        if ($AllowNotFound -and $statusCode -eq 404) {
            return $null
        }

        throw
    }
}

function Invoke-Health {
    param([string] $Name, [string] $Uri)

    $health = Invoke-RestMethod -Method GET -Uri $Uri
    Write-Host "$Name health: $($health.status)"
}

Write-Host "Winery Management System showcase"
Write-Host "Scenario: complete a production batch and verify that inventory stock is updated through JMS."

Write-Step "Checking service health"
Invoke-Health "Harvest service" "$HarvestBaseUrl/actuator/health"
Invoke-Health "Production service" "$ProductionBaseUrl/actuator/health"
Invoke-Health "Inventory service" "$InventoryBaseUrl/actuator/health"
Invoke-Health "Customer service" "$CustomerBaseUrl/actuator/health"

Write-Step "Reading harvest seed data"
$harvestRecords = Invoke-Api -Method GET -Uri "$HarvestBaseUrl/harvest-records"
$harvestByYear = Invoke-Api -Method GET -Uri "$HarvestBaseUrl/harvest-records/summary/by-year"
Write-Host "Harvest records loaded: $($harvestRecords.Count)"
Write-Host "Harvest yearly summary entries: $($harvestByYear.Count)"

Write-Step "Reading selected production batch"
$batch = Invoke-Api -Method GET -Uri "$ProductionBaseUrl/production/batches/$BatchId"
$expectedBottleIncrease = $batch.actualBottleCount
if ($expectedBottleIncrease -le 0) {
    $expectedBottleIncrease = $batch.plannedBottleCount
}
Write-Host "Batch id: $($batch.id)"
Write-Host "Batch status before completion: $($batch.status)"
Write-Host "Wine id: $($batch.recipeId)"
Write-Host "Expected inventory increase: $expectedBottleIncrease bottles"

if ($batch.status -eq "COMPLETED") {
    throw "The showcase batch is already COMPLETED. Restart the stack with fresh in-memory databases and production seed data, then run the script again."
}

Write-Step "Reading inventory stock before completion"
$stockBefore = Invoke-Api -Method GET -Uri "$InventoryBaseUrl/stocks/wine/$WineId" -AllowNotFound
if ($null -eq $stockBefore) {
    $quantityBefore = 0
    Write-Host "No existing stock for wine $WineId. Inventory should create it from the JMS message."
} else {
    $quantityBefore = $stockBefore.quantity
    Write-Host "Stock quantity before completion: $quantityBefore"
}

Write-Step "Completing production batch"
$completedBatch = Invoke-Api -Method PATCH -Uri "$ProductionBaseUrl/production/batches/$BatchId/status?status=COMPLETED"
Write-Host "Batch status after completion: $($completedBatch.status)"

if ($completedBatch.status -ne "COMPLETED") {
    throw "Production batch was not marked as COMPLETED."
}

Write-Step "Verifying inventory stock after JMS processing"
$stockAfter = $null
for ($attempt = 1; $attempt -le 10; $attempt++) {
    Start-Sleep -Seconds 1
    $stockAfter = Invoke-Api -Method GET -Uri "$InventoryBaseUrl/stocks/wine/$WineId" -AllowNotFound
    if ($null -ne $stockAfter -and $stockAfter.quantity -ge ($quantityBefore + $expectedBottleIncrease)) {
        break
    }
}

if ($null -eq $stockAfter) {
    throw "Inventory stock for wine $WineId was not created."
}

$quantityAfter = $stockAfter.quantity
$actualIncrease = $quantityAfter - $quantityBefore

Write-Host "Stock quantity after completion: $quantityAfter"
Write-Host "Actual inventory increase: $actualIncrease bottles"

if ($actualIncrease -lt $expectedBottleIncrease) {
    throw "Inventory stock did not increase by the expected amount. Expected at least $expectedBottleIncrease, got $actualIncrease."
}

Write-Step "Showcase finished successfully"
Write-Host "The production batch completion triggered asynchronous inventory stock update."
