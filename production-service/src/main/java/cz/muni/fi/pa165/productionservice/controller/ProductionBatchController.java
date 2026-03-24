package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateRequestDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionBatchFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/production/batches")
@Tag(name = "Production Batches", description = "Endpoints for managing production batches")
public class ProductionBatchController {
    private final ProductionBatchFacade productionBatchFacade;

    @Autowired
    public ProductionBatchController(ProductionBatchFacade productionBatchFacade) {
        this.productionBatchFacade = productionBatchFacade;
    }

    @PostMapping
    @Operation(summary = "Create a production batch from a requested bottle count")
    public ResponseEntity<ProductionBatchDto> createBatch(@Valid @RequestBody ProductionBatchCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productionBatchFacade.createBatch(request.getBatch(), request.getHarvestLots()));
    }

    @GetMapping
    @Operation(summary = "Get all production batches")
    public ResponseEntity<List<ProductionBatchDto>> getBatches() {
        return ResponseEntity.ok(productionBatchFacade.findAllBatches());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a production batch by id")
    public ResponseEntity<ProductionBatchDto> getBatch(@PathVariable UUID id) {
        return ResponseEntity.ok(productionBatchFacade.findBatch(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a production batch by id")
    public ResponseEntity<Void> deleteBatch(@PathVariable UUID id) {
        productionBatchFacade.deleteBatch(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update a production batch status")
    public ResponseEntity<ProductionBatchDto> updateBatchStatus(@PathVariable UUID id, @RequestParam BatchStatus status) {
        return ResponseEntity.ok(productionBatchFacade.updateBatchStatus(id, status));
    }
}
