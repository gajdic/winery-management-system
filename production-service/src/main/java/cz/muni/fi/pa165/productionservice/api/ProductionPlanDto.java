package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Calculated production plan for requested bottle count and available harvests.")
public class ProductionPlanDto {
    @Schema(description = "Recipe used for the production plan.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;
    @Schema(description = "Requested number of bottles.", example = "1000")
    private int requestedBottleCount;
    @Schema(description = "Number of bottles feasible with available harvest lots.", example = "850")
    private int feasibleBottleCount;
    @Schema(description = "Wine volume that can be produced in liters.", example = "637.5")
    private double producibleLiters;
    @Schema(description = "Whether all requested bottles can be produced.", example = "false")
    private boolean fullyFeasible;
    @Schema(description = "Grape type limiting production capacity, if production is not fully feasible.", example = "Riesling", nullable = true)
    private String limitingGrapeType;
    @Schema(description = "Estimated weighted blend quality score.", example = "87")
    private int estimatedBlendQuality;
}
