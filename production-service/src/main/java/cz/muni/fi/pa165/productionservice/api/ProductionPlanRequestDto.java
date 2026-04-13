package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ProductionPlanRequestDto {
    @NotNull
    private UUID recipeId;

    @PositiveOrZero
    private int desiredBottleCount;

    @Valid
    @NotEmpty
    private List<HarvestLotDto> availableHarvests = new ArrayList<>();

    public void setAvailableHarvests(List<HarvestLotDto> availableHarvests) {
        this.availableHarvests = availableHarvests != null ? availableHarvests : new ArrayList<>();
    }
}
