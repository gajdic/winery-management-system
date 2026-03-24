package cz.muni.fi.pa165.productionservice.rest;

import cz.muni.fi.pa165.productionservice.api.*;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionBranchFacade;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
public class ProductionController {
    private final WineRecipeFacade wineRecipeFacade;
    private final ProductionBranchFacade productionBranchFacade;

    public ProductionController(
        WineRecipeFacade wineRecipeFacade,
        ProductionBranchFacade productionBranchFacade
    ) {
        this.wineRecipeFacade = wineRecipeFacade;
        this.productionBranchFacade = productionBranchFacade;
    }

    @PostMapping("/recipes")
    @Operation(summary = "Create a wine recipe", tags = {"Recipes"})
    public ResponseEntity<WineRecipeDto> createRecipe(@Valid @RequestBody WineRecipeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wineRecipeFacade.createRecipe(dto));
    }

    @GetMapping("/recipes")
    @Operation(summary = "Get all wine recipes", tags = {"Recipes"})
    public ResponseEntity<List<WineRecipeDto>> getRecipes() {
        return ResponseEntity.ok(wineRecipeFacade.findAllRecipes());
    }

    @GetMapping("/recipes/{id}")
    @Operation(summary = "Get a wine recipe by id", tags = {"Recipes"})
    public ResponseEntity<WineRecipeDto> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(wineRecipeFacade.findRecipe(id));
    }

    @PostMapping("/planning")
    @Operation(summary = "Plan production from harvest lots and recipe requirements", tags = {"Planning"})
    public ResponseEntity<ProductionPlanDto> planProduction(@Valid @RequestBody ProductionPlanRequestDto dto) {
        return ResponseEntity.ok(wineRecipeFacade.planProduction(dto));
    }

    @PostMapping("/quality-evaluations")
    @Operation(summary = "Evaluate expected blend quality for a recipe and harvest set", tags = {"Planning"})
    public ResponseEntity<QualityEvaluationDto> evaluateQuality(@Valid @RequestBody QualityEvaluationRequestDto dto) {
        return ResponseEntity.ok(wineRecipeFacade.evaluateQuality(dto));
    }

    @PostMapping("/batches")
    @Operation(summary = "Create a production batch from a requested bottle count", tags = {"Batches"})
    public ResponseEntity<ProductionBatchDto> createBatch(@Valid @RequestBody ProductionBatchCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productionBranchFacade.createBatch(request.getBatch(), request.getHarvestLots()));
    }

    @GetMapping("/batches")
    @Operation(summary = "Get all production batches", tags = {"Batches"})
    public ResponseEntity<List<ProductionBatchDto>> getBatches() {
        return ResponseEntity.ok(productionBranchFacade.findAllBatches());
    }

    @GetMapping("/batches/{id}")
    @Operation(summary = "Get a production batch by id", tags = {"Batches"})
    public ResponseEntity<ProductionBatchDto> getBatch(@PathVariable Long id) {
        return ResponseEntity.ok(productionBranchFacade.findBatch(id));
    }

    @PatchMapping("/batches/{id}/status")
    @Operation(summary = "Update a production batch status", tags = {"Batches"})
    public ResponseEntity<ProductionBatchDto> updateBatchStatus(@PathVariable Long id, @RequestParam BatchStatus status) {
        return ResponseEntity.ok(productionBranchFacade.updateBatchStatus(id, status));
    }

}
