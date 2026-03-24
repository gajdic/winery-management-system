package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QualityEvaluationRequestDto {
    @NotNull
    private Long recipeId;

    @Valid
    private List<HarvestLotDto> harvestLots;

    public QualityEvaluationRequestDto() {
        harvestLots = new ArrayList<>();
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public List<HarvestLotDto> getHarvestLots() {
        return harvestLots;
    }

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots;
    }
}
