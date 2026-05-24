package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.commonservice.openapi.SwaggerConstants;
import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.config.SecurityConfig;
import cz.muni.fi.pa165.productionservice.facade.ProductionPlanningFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
@Tag(name = "Production Planning", description = "Endpoints for production planning and quality evaluation")
public class ProductionPlanningController {
    private final ProductionPlanningFacade productionPlanningFacade;

    @Autowired
    public ProductionPlanningController(ProductionPlanningFacade productionPlanningFacade) {
        this.productionPlanningFacade = productionPlanningFacade;
    }

    @PostMapping("/planning")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SwaggerConstants.READ_SCOPE)
    @Operation(
        summary = "Plan production from harvest lots and recipe requirements",
        description = "Calculates how many bottles can be produced for a recipe from the provided harvest lots."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production plan calculated",
            content = @Content(schema = @Schema(implementation = ProductionPlanDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body or planning input",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductionPlanDto> planProduction(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Production planning request with recipe id, desired bottles, and available harvests.",
            required = true,
            content = @Content(schema = @Schema(implementation = ProductionPlanRequestDto.class))
        )
        @RequestBody ProductionPlanRequestDto dto
    ) {
        return ResponseEntity.ok(productionPlanningFacade.planProduction(dto));
    }

    @PostMapping("/quality-evaluations")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SwaggerConstants.READ_SCOPE)
    @Operation(
        summary = "Evaluate expected blend quality for a recipe and harvest set",
        description = "Calculates weighted quality and recommendation for a recipe using the supplied harvest lots."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality evaluation calculated",
            content = @Content(schema = @Schema(implementation = QualityEvaluationDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body or quality evaluation input",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<QualityEvaluationDto> evaluateQuality(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Quality evaluation request with recipe id and harvest lots.",
            required = true,
            content = @Content(schema = @Schema(implementation = QualityEvaluationRequestDto.class))
        )
        @RequestBody QualityEvaluationRequestDto dto
    ) {
        return ResponseEntity.ok(productionPlanningFacade.evaluateQuality(dto));
    }
}
