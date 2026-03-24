package cz.muni.fi.pa165.productionservice.rest;

import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
@Tag(name = "Production Service", description = "Management of wine recipes, production planning and production batches")
public class ProductionController {
    private final ProductionFacade productionFacade;

    public ProductionController(ProductionFacade productionFacade) {
        this.productionFacade = productionFacade;
    }

    @PostMapping("/recipes")
    @Operation(summary = "Create a wine recipe")
    public ResponseEntity<WineRecipeDto> createRecipe(@Valid @RequestBody WineRecipeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productionFacade.createRecipe(dto));
    }

    @GetMapping("/recipes")
    @Operation(summary = "Get all wine recipes")
    public ResponseEntity<List<WineRecipeDto>> getRecipes() {
        return ResponseEntity.ok(productionFacade.findAllRecipes());
    }

    @GetMapping("/recipes/{id}")
    @Operation(summary = "Get a wine recipe by id")
    public ResponseEntity<WineRecipeDto> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(productionFacade.findRecipe(id));
    }

    @PostMapping("/planning")
    @Operation(summary = "Plan production from harvest lots and recipe requirements")
    public ResponseEntity<ProductionPlanDto> planProduction(@Valid @RequestBody ProductionPlanRequestDto dto) {
        return ResponseEntity.ok(productionFacade.planProduction(dto));
    }

    @PostMapping("/quality-evaluations")
    @Operation(summary = "Evaluate expected blend quality for a recipe and harvest set")
    public ResponseEntity<QualityEvaluationDto> evaluateQuality(@Valid @RequestBody QualityEvaluationRequestDto dto) {
        return ResponseEntity.ok(productionFacade.evaluateQuality(dto));
    }

    @PostMapping("/batches")
    @Operation(summary = "Create a production batch from a requested bottle count")
    public ResponseEntity<ProductionBatchDto> createBatch(@Valid @RequestBody ProductionBatchCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productionFacade.createBatch(request.getBatch(), request.getHarvestLots()));
    }

    @GetMapping("/batches")
    @Operation(summary = "Get all production batches")
    public ResponseEntity<List<ProductionBatchDto>> getBatches() {
        return ResponseEntity.ok(productionFacade.findAllBatches());
    }

    @GetMapping("/batches/{id}")
    @Operation(summary = "Get a production batch by id")
    public ResponseEntity<ProductionBatchDto> getBatch(@PathVariable Long id) {
        return ResponseEntity.ok(productionFacade.findBatch(id));
    }

    @PatchMapping("/batches/{id}/status")
    @Operation(summary = "Update a production batch status")
    public ResponseEntity<ProductionBatchDto> updateBatchStatus(@PathVariable Long id, @RequestParam BatchStatus status) {
        return ResponseEntity.ok(productionFacade.updateBatchStatus(id, status));
    }

}
