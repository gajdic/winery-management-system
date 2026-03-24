package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductionBatchCreateRequestDto {
    @Valid
    @NotNull
    private ProductionBatchCreateDto batch;

    @Valid
    @NotEmpty
    private List<HarvestLotDto> harvestLots;

    public ProductionBatchCreateDto getBatch() {
        return batch;
    }

    public void setBatch(ProductionBatchCreateDto batch) {
        this.batch = batch;
    }

    public List<HarvestLotDto> getHarvestLots() {
        return harvestLots;
    }

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots != null ? harvestLots : new ArrayList<>();
    }
}
