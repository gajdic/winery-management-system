package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Request for creating a production batch from a recipe and available harvest lots.")
public class ProductionBatchCreateRequestDto {
    @Valid
    @NotNull
    @Schema(description = "Batch production target.")
    private ProductionBatchCreateDto batch;

    @Valid
    @NotEmpty
    @Schema(description = "Harvest lots available for planning the batch.")
    private List<HarvestLotDto> harvestLots = new ArrayList<>();

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots != null ? harvestLots : new ArrayList<>();
    }
}
