package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.PositiveOrZero;

public class QualityEvaluationDto {
    private Long recipeId;

    @PositiveOrZero
    private int weightedQualityScore;

    private boolean recipeQualityTargetMet;
    private String recommendation;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public int getWeightedQualityScore() {
        return weightedQualityScore;
    }

    public void setWeightedQualityScore(int weightedQualityScore) {
        this.weightedQualityScore = weightedQualityScore;
    }

    public boolean isRecipeQualityTargetMet() {
        return recipeQualityTargetMet;
    }

    public void setRecipeQualityTargetMet(boolean recipeQualityTargetMet) {
        this.recipeQualityTargetMet = recipeQualityTargetMet;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
