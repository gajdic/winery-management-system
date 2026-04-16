package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Request for calculating feasible wine production from available harvests.")
public class ProductionPlanRequestDto {
    @NotNull
    @Schema(description = "Recipe to plan production for.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;

    @PositiveOrZero
    @Schema(description = "Desired number of bottles.", example = "1000", minimum = "0")
    private int desiredBottleCount;

    @Valid
    @NotEmpty
    @Schema(description = "Harvest lots available for production planning.")
    private List<HarvestLotDto> availableHarvests = new ArrayList<>();

    public void setAvailableHarvests(List<HarvestLotDto> availableHarvests) {
        this.availableHarvests = availableHarvests != null ? availableHarvests : new ArrayList<>();
    }
}
