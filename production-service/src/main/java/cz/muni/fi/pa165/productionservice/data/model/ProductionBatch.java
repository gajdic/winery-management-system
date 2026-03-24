package cz.muni.fi.pa165.productionservice.data.model;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;

import java.time.LocalDateTime;

public class ProductionBatch {
    private Long id;
    private Long recipeId;
    private double plannedLiters;
    private int plannedBottleCount;
    private int actualBottleCount;
    private BatchStatus status;
    private LocalDateTime createdAt;

    public ProductionBatch() {
    }

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
