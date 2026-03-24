package cz.muni.fi.pa165.productionservice.api;

import java.util.UUID;

public class QualityEvaluationDto {
    private UUID recipeId;
    private int weightedQualityScore;
    private boolean recipeQualityTargetMet;
    private String recommendation;

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
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
