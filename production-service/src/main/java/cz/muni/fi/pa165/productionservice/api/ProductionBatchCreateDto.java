package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class ProductionBatchCreateDto {
    @NotNull
    private UUID recipeId;

    @Positive
    private int desiredBottleCount;

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public int getDesiredBottleCount() {
        return desiredBottleCount;
    }

    public void setDesiredBottleCount(int desiredBottleCount) {
        this.desiredBottleCount = desiredBottleCount;
    }
}
