package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductionBatchCreateRequestDto {
    @Valid
    @NotNull
    private ProductionBatchCreateDto batch;

    @Valid
    @NotEmpty
    private List<HarvestLotDto> harvestLots = new ArrayList<>();

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots != null ? harvestLots : new ArrayList<>();
    }
}
