package cz.muni.fi.pa165.productionservice.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Recommendation produced by quality evaluation.")
public enum QualityRecommendation {
    @Schema(description = "Blend quality is excellent and suitable for a premium production batch.")
    PREMIUM_BATCH_CANDIDATE,
    @Schema(description = "Blend quality is acceptable and suitable for standard production.")
    SUITABLE_FOR_PRODUCTION,
    @Schema(description = "Blend quality is below the recipe target and requires adjustment or rejection.")
    BELOW_TARGET_QUALITY
}
