package cz.muni.fi.pa165.productionservice.api;

import cz.muni.fi.pa165.productionservice.api.enums.QualityRecommendation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Quality evaluation result for a recipe and harvest set.")
public class QualityEvaluationDto {
    @Schema(description = "Recipe evaluated for expected blend quality.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;
    @Schema(description = "Weighted quality score calculated from matching harvest lots.", example = "87")
    private int weightedQualityScore;
    @Schema(description = "Whether the recipe quality target is met.", example = "true")
    private boolean recipeQualityTargetMet;
    @Schema(description = "Recommended production decision based on quality.")
    private QualityRecommendation recommendation;
}
