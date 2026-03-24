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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public double getPlannedLiters() {
        return plannedLiters;
    }

    public void setPlannedLiters(double plannedLiters) {
        this.plannedLiters = plannedLiters;
    }

    public int getPlannedBottleCount() {
        return plannedBottleCount;
    }

    public void setPlannedBottleCount(int plannedBottleCount) {
        this.plannedBottleCount = plannedBottleCount;
    }

    public int getActualBottleCount() {
        return actualBottleCount;
    }

    public void setActualBottleCount(int actualBottleCount) {
        this.actualBottleCount = actualBottleCount;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
