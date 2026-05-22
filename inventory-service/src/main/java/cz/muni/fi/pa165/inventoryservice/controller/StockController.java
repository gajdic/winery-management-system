package cz.muni.fi.pa165.inventoryservice.controller;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.api.StockQuantityChangeDto;
import cz.muni.fi.pa165.inventoryservice.config.SecurityConfig;
import cz.muni.fi.pa165.inventoryservice.facade.StockFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stocks")
@Tag(name = "Stocks", description = "Endpoints for managing wine stock quantities and reservations")
public class StockController {

    private final StockFacade stockFacade;

    @Autowired
    public StockController(StockFacade stockFacade) {
        this.stockFacade = stockFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create a stock",
        description = "Creates a new stock entry for a wine item",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Stock created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid stock payload"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope")
    })
    public ResponseEntity<StockDetailedViewDto> create(@RequestBody @Valid StockDetailedViewDto stock) {
        StockDetailedViewDto created = stockFacade.create(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a stock by id",
        description = "Returns one stock entry by its unique identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<StockDetailedViewDto> findById(@PathVariable UUID id) {
        StockDetailedViewDto stock = stockFacade.findById(id);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @GetMapping
    @Operation(
        summary = "List all stocks",
        description = "Returns all stock entries currently registered in the inventory service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stocks returned successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope")
    })
    public ResponseEntity<List<StockDetailedViewDto>> findAll() {
        return ResponseEntity.ok(stockFacade.findAll());
    }

    @GetMapping("/wine/{wineId}")
    @Operation(
        summary = "Get a stock by wine id",
        description = "Returns the stock entry associated with the given wine identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found for the given wine")
    })
    public ResponseEntity<StockDetailedViewDto> findByWine(@PathVariable UUID wineId) {
        StockDetailedViewDto stock = stockFacade.findByWine(wineId);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/{id}/add")
    @Operation(
        summary = "Add quantity to a stock",
        description = "Increases the stock quantity by the value supplied in the request body",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock quantity updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity payload"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<StockDetailedViewDto> addToStock(@PathVariable UUID id, @RequestBody @Valid StockQuantityChangeDto change) {
        StockDetailedViewDto updated = stockFacade.addToStock(id, change.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/remove")
    @Operation(
        summary = "Remove quantity from a stock",
        description = "Decreases the stock quantity by the value supplied in the request body",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock quantity updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity payload or insufficient stock"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<StockDetailedViewDto> removeFromStock(@PathVariable UUID id, @RequestBody @Valid StockQuantityChangeDto change) {
        StockDetailedViewDto updated = stockFacade.removeFromStock(id, change.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a stock",
        description = "Updates an existing stock entry by its identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid stock payload"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<StockDetailedViewDto> update(@PathVariable UUID id, @RequestBody @Valid StockDetailedViewDto stock) {
        stock.setId(id);
        StockDetailedViewDto updated = stockFacade.update(stock);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a stock",
        description = "Removes a stock entry from the inventory service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Stock deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication is required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = stockFacade.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
