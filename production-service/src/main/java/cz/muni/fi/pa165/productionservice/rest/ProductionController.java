package cz.muni.fi.pa165.productionservice.rest;

import cz.muni.fi.pa165.productionservice.api.*;
import cz.muni.fi.pa165.productionservice.facade.ProductionPlanningFacade;
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
    private final ProductionPlanningFacade productionPlanningFacade;

    public ProductionController(
        WineRecipeFacade wineRecipeFacade,
        ProductionPlanningFacade productionPlanningFacade
    ) {
        this.wineRecipeFacade = wineRecipeFacade;
        this.productionPlanningFacade = productionPlanningFacade;
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
        return ResponseEntity.ok(productionPlanningFacade.planProduction(dto));
    }

    @PostMapping("/quality-evaluations")
    @Operation(summary = "Evaluate expected blend quality for a recipe and harvest set", tags = {"Planning"})
    public ResponseEntity<QualityEvaluationDto> evaluateQuality(@Valid @RequestBody QualityEvaluationRequestDto dto) {
        return ResponseEntity.ok(productionPlanningFacade.evaluateQuality(dto));
    }
}
