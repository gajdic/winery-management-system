package cz.muni.fi.pa165.productionservice.data.model;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductionBatch {
    private UUID id;
    private UUID recipeId;
    private double plannedLiters;
    private int plannedBottleCount;
    private int actualBottleCount;
    private BatchStatus status;
    private LocalDateTime createdAt;

    public ProductionBatch() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
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
