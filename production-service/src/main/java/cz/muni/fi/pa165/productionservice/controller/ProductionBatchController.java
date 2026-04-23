package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateRequestDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionBatchFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/production/batches")
@Tag(name = "Production Batches", description = "Endpoints for managing production batches")
public class ProductionBatchController {
    private final ProductionBatchFacade productionBatchFacade;

    @Autowired
    public ProductionBatchController(ProductionBatchFacade productionBatchFacade) {
        this.productionBatchFacade = productionBatchFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create a production batch from a requested bottle count",
        description = "Creates a planned batch by calculating feasible production from the selected recipe and supplied harvest lots."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Production batch created",
            content = @Content(schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body or production input",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductionBatchDto> createBatch(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Production batch creation request.",
            required = true,
            content = @Content(schema = @Schema(implementation = ProductionBatchCreateRequestDto.class))
        )
        @RequestBody ProductionBatchCreateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productionBatchFacade.createBatch(request.getBatch(), request.getHarvestLots()));
    }

    @GetMapping
    @Operation(
        summary = "Get all production batches",
        description = "Returns all production batches managed by the production service."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production batches returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductionBatchDto.class)))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ProductionBatchDto>> getBatches() {
        return ResponseEntity.ok(productionBatchFacade.findAllBatches());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a production batch by id",
        description = "Returns a single production batch identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production batch returned",
            content = @Content(schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Production batch not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductionBatchDto> getBatch(
        @Parameter(description = "Production batch identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(productionBatchFacade.findBatch(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a production batch by id",
        description = "Deletes an existing production batch identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Production batch deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Production batch not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteBatch(
        @Parameter(description = "Production batch identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id
    ) {
        productionBatchFacade.deleteBatch(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Update a production batch status",
        description = "Changes the lifecycle status of an existing production batch."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production batch status updated",
            content = @Content(schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format or status value",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Production batch not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductionBatchDto> updateBatchStatus(
        @Parameter(description = "Production batch identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id,
        @Parameter(description = "New production batch status.", example = "IN_PRODUCTION")
        @RequestParam @NotNull BatchStatus status
    ) {
        return ResponseEntity.ok(productionBatchFacade.updateBatchStatus(id, status));
    }
}
