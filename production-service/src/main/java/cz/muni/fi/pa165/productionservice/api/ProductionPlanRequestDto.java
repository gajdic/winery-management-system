package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductionPlanRequestDto {
    @NotNull
    private UUID recipeId;

    @PositiveOrZero
    private int desiredBottleCount;

    @Valid
    private List<HarvestLotDto> availableHarvests;

    public ProductionPlanRequestDto() {
        availableHarvests = new ArrayList<>();
    }

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

    public List<HarvestLotDto> getAvailableHarvests() {
        return availableHarvests;
    }

    public void setAvailableHarvests(List<HarvestLotDto> availableHarvests) {
        this.availableHarvests = availableHarvests;
    }
}
