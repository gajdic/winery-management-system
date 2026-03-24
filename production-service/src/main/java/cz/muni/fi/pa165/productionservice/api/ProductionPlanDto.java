package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductionPlanDto {
    private Long recipeId;

    @PositiveOrZero
    private int requestedBottleCount;

    @PositiveOrZero
    private int feasibleBottleCount;

    @PositiveOrZero
    private double producibleLiters;
    private boolean fullyFeasible;

    @NotBlank
    private String limitingGrapeType;

    @Min(0)
    @Max(100)
    private int estimatedBlendQuality;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
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
