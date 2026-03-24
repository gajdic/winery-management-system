package cz.muni.fi.pa165.productionservice.rest;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.facade.ProductionPlanningFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@Tag(name = "Production Planning", description = "Endpoints for production planning and quality evaluation")
public class ProductionPlanningController {
    private final ProductionPlanningFacade productionPlanningFacade;

    @Autowired
    public ProductionPlanningController(ProductionPlanningFacade productionPlanningFacade) {
        this.productionPlanningFacade = productionPlanningFacade;
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
