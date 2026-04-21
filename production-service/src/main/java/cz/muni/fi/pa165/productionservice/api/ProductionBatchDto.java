package cz.muni.fi.pa165.productionservice.api;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductionBatchDto {
    @NotNull
    private UUID id;

    @NotNull
    private UUID recipeId;

    @PositiveOrZero
    private double plannedLiters;

    @PositiveOrZero
    private int plannedBottleCount;

    @PositiveOrZero
    private int actualBottleCount;
    private BatchStatus status;

    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;
}
