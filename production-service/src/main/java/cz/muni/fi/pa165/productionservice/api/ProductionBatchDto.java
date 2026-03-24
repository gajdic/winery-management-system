package cz.muni.fi.pa165.productionservice.api;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public class ProductionBatchDto {
    private Long id;
    private Long recipeId;

    @PositiveOrZero
    private double plannedLiters;

    @PositiveOrZero
    private int plannedBottleCount;

    @PositiveOrZero
    private int actualBottleCount;
    private BatchStatus status;

    @NotNull
    private LocalDateTime createdAt;

}
