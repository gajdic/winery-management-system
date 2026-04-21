package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class HarvestLotDto {
    @NotNull
    private UUID grapeTypeId;

    @NotNull
    private UUID vineyardId;

    @NotNull
    @PastOrPresent
    private LocalDate harvestDate;

    @PositiveOrZero
    private double availableKilograms;

    @Min(0)
    @Max(100)
    private int qualityScore;
}
