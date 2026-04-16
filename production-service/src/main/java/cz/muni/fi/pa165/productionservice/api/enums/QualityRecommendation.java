package cz.muni.fi.pa165.productionservice.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Recommendation produced by quality evaluation.")
public enum QualityRecommendation {
    PREMIUM_BATCH_CANDIDATE,
    SUITABLE_FOR_PRODUCTION,
    BELOW_TARGET_QUALITY
}
