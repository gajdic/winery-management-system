package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Available harvest lot that can be used for production planning.")
public class HarvestLotDto {
    @NotNull
    @Schema(description = "Identifier of the harvested grape type.", example = "11111111-1111-1111-1111-111111111111")
    private UUID grapeTypeId;

    @NotNull
    @Schema(description = "Identifier of the vineyard where grapes were harvested.", example = "22222222-2222-2222-2222-222222222222")
    private UUID vineyardId;

    @NotNull
    @PastOrPresent
    @Schema(description = "Date when the harvest lot was collected.", example = "2025-09-20")
    private LocalDate harvestDate;

    @PositiveOrZero
    @Schema(description = "Available grape quantity in kilograms.", example = "1200.5", minimum = "0")
    private double availableKilograms;

    @Min(0)
    @Max(100)
    @Schema(description = "Measured harvest quality score.", example = "88", minimum = "0", maximum = "100")
    private int qualityScore;
}
