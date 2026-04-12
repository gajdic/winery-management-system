package cz.muni.fi.pa165.productionservice.api;

import lombok.Data;

import java.util.UUID;

@Data
public class QualityEvaluationDto {
    private UUID recipeId;
    private int weightedQualityScore;
    private boolean recipeQualityTargetMet;
    private QualityRecommendation recommendation;
}
