package cz.muni.fi.pa165.productionservice.api;

import java.util.UUID;

public class ProductionPlanDto {
    private UUID recipeId;
    private int requestedBottleCount;
    private int feasibleBottleCount;
    private double producibleLiters;
    private boolean fullyFeasible;
    private String limitingGrapeType;
    private int estimatedBlendQuality;

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public int getRequestedBottleCount() {
        return requestedBottleCount;
    }

    public void setRequestedBottleCount(int requestedBottleCount) {
        this.requestedBottleCount = requestedBottleCount;
    }

    public int getFeasibleBottleCount() {
        return feasibleBottleCount;
    }

    public void setFeasibleBottleCount(int feasibleBottleCount) {
        this.feasibleBottleCount = feasibleBottleCount;
    }

    public double getProducibleLiters() {
        return producibleLiters;
    }

    public void setProducibleLiters(double producibleLiters) {
        this.producibleLiters = producibleLiters;
    }

    public boolean isFullyFeasible() {
        return fullyFeasible;
    }

    public void setFullyFeasible(boolean fullyFeasible) {
        this.fullyFeasible = fullyFeasible;
    }

    public String getLimitingGrapeType() {
        return limitingGrapeType;
    }

    public void setLimitingGrapeType(String limitingGrapeType) {
        this.limitingGrapeType = limitingGrapeType;
    }

    public int getEstimatedBlendQuality() {
        return estimatedBlendQuality;
    }

    public void setEstimatedBlendQuality(int estimatedBlendQuality) {
        this.estimatedBlendQuality = estimatedBlendQuality;
    }
}
