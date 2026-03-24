package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlanRequestDto {
    @NotNull
    private Long recipeId;

    @PositiveOrZero
    private int desiredBottleCount;

    @Valid
    private List<HarvestLotDto> availableHarvests;

    public ProductionPlanRequestDto() {
        availableHarvests = new ArrayList<>();
    }

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

    public List<HarvestLotDto> getAvailableHarvests() {
        return availableHarvests;
    }

    public void setAvailableHarvests(List<HarvestLotDto> availableHarvests) {
        this.availableHarvests = availableHarvests;
    }
}
