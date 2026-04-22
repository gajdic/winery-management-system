package cz.muni.fi.pa165.productionservice.api;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Production batch with planned and actual bottle counts.")
public class ProductionBatchDto {
    @NotNull
    @Schema(description = "Production batch identifier.", example = "44444444-4444-4444-4444-444444444444")
    private UUID id;

    @NotNull
    @Schema(description = "Recipe used by the production batch.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;

    @PositiveOrZero
    @Schema(description = "Planned produced wine volume in liters.", example = "750.0", minimum = "0")
    private double plannedLiters;

    @PositiveOrZero
    @Schema(description = "Planned number of bottles.", example = "1000", minimum = "0")
    private int plannedBottleCount;

    @PositiveOrZero
    @Schema(description = "Actual number of bottles produced.", example = "980", minimum = "0")
    private int actualBottleCount;
    @Schema(description = "Current production batch lifecycle status.")
    private BatchStatus status;

    @NotNull
    @PastOrPresent
    @Schema(description = "UTC timestamp when the batch was created.", example = "2026-04-16T12:30:00")
    private LocalDateTime createdAt;
}
