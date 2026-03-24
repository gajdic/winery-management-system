package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QualityEvaluationRequestDto {
    @NotNull
    private UUID recipeId;

    @Valid
    @NotEmpty
    private List<HarvestLotDto> harvestLots;

    public QualityEvaluationRequestDto() {
        harvestLots = new ArrayList<>();
    }

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public List<HarvestLotDto> getHarvestLots() {
        return harvestLots;
    }

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots;
    }
}
