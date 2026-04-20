package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.facade.PurchaseRecordFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
@Tag(name = "Purchases", description = "Purchase record management endpoints")
public class PurchaseRecordController {

    private final PurchaseRecordFacade purchaseRecordFacade;

    public PurchaseRecordController(PurchaseRecordFacade purchaseRecordFacade) {
        this.purchaseRecordFacade = purchaseRecordFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create purchase record",
        description = "Creates a new purchase record for a customer and wine."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Purchase record created",
            content = @Content(schema = @Schema(implementation = PurchaseRecordDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<PurchaseRecordDto> createPurchase(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Purchase record creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = PurchaseRecordCreateDto.class))
        )
        @RequestBody PurchaseRecordCreateDto dto
    ) {
        PurchaseRecordDto created = purchaseRecordFacade.createPurchase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get purchase by ID",
        description = "Returns a purchase record identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchase found",
            content = @Content(schema = @Schema(implementation = PurchaseRecordDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Purchase not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<PurchaseRecordDto> getPurchase(
        @Parameter(description = "Purchase ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(purchaseRecordFacade.getPurchase(id));
    }

    @GetMapping
    @Operation(
        summary = "Get all purchases",
        description = "Returns all purchase records."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchases retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PurchaseRecordDto.class))))
    })
    public ResponseEntity<List<PurchaseRecordDto>> getAllPurchases() {
        return ResponseEntity.ok(purchaseRecordFacade.getAllPurchases());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(
        summary = "Get purchases by customer",
        description = "Returns all purchase records for a given customer."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchases retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PurchaseRecordDto.class))))
    })
    public ResponseEntity<List<PurchaseRecordDto>> getByCustomerId(
        @Parameter(description = "Customer ID", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID customerId
    ) {
        return ResponseEntity.ok(purchaseRecordFacade.getByCustomerId(customerId));
    }

    @GetMapping("/wine/{wineId}")
    @Operation(
        summary = "Get purchases by wine",
        description = "Returns all purchase records for a given wine."
    )
    public ResponseEntity<List<PurchaseRecordDto>> getByWineId(
        @Parameter(description = "Wine ID", example = "33333333-3333-3333-3333-333333333333")
        @PathVariable UUID wineId
    ) {
        return ResponseEntity.ok(purchaseRecordFacade.getByWineId(wineId));
    }

    @GetMapping("/quantity")
    @Operation(
        summary = "Filter purchases by quantity",
        description = "Returns purchases filtered by optional min and max quantity."
    )
    public ResponseEntity<List<PurchaseRecordDto>> getByQuantity(
        @Parameter(description = "Minimum quantity", example = "1")
        @RequestParam(required = false) Integer min,

        @Parameter(description = "Maximum quantity", example = "10")
        @RequestParam(required = false) Integer max
    ) {
        return ResponseEntity.ok(purchaseRecordFacade.getByQuantity(min, max));
    }

    @GetMapping("/customer/{customerId}/total-spent")
    @Operation(
        summary = "Get total spent by customer",
        description = "Calculates total amount spent by a given customer."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total calculated",
            content = @Content(schema = @Schema(implementation = Double.class)))
    })
    public ResponseEntity<Double> getTotalSpent(
        @Parameter(description = "Customer ID", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID customerId
    ) {
        return ResponseEntity.ok(purchaseRecordFacade.getTotalSpentByCustomer(customerId));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete purchase",
        description = "Deletes a purchase record by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Purchase deleted"),
        @ApiResponse(responseCode = "404", description = "Purchase not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deletePurchase(
        @Parameter(description = "Purchase ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        purchaseRecordFacade.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
