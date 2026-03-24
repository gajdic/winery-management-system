package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductionBatchCreateDto {
    @NotNull
    private Long recipeId;

    @Positive
    private int desiredBottleCount;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public int getDesiredBottleCount() {
        return desiredBottleCount;
    }

    public void setDesiredBottleCount(int desiredBottleCount) {
        this.desiredBottleCount = desiredBottleCount;
    }
}
