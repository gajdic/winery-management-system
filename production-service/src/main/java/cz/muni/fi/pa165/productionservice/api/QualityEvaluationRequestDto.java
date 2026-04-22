package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Request for evaluating expected blend quality from harvest lots.")
public class QualityEvaluationRequestDto {
    @NotNull
    @Schema(description = "Recipe to evaluate.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;

    @Valid
    @NotEmpty
    @Schema(description = "Harvest lots used for quality evaluation.")
    private List<HarvestLotDto> harvestLots = new ArrayList<>();

    public void setHarvestLots(List<HarvestLotDto> harvestLots) {
        this.harvestLots = harvestLots != null ? harvestLots : new ArrayList<>();
    }
}
