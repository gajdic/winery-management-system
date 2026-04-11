package cz.muni.fi.pa165.productionservice.data.model;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "production_batches")
public class ProductionBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID recipeId;
    private double plannedLiters;
    private int plannedBottleCount;
    private int actualBottleCount;
    @Enumerated(EnumType.STRING)
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductionBatch that = (ProductionBatch) o;
        return Double.compare(plannedLiters, that.plannedLiters) == 0 && plannedBottleCount == that.plannedBottleCount && actualBottleCount == that.actualBottleCount && Objects.equals(recipeId, that.recipeId) && status == that.status && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, plannedLiters, plannedBottleCount, actualBottleCount, status, createdAt);
    }

    @Override
    public String toString() {
        return "ProductionBatch{" +
            "id=" + id +
            ", recipeId=" + recipeId +
            ", plannedLiters=" + plannedLiters +
            ", plannedBottleCount=" + plannedBottleCount +
            ", actualBottleCount=" + actualBottleCount +
            ", status=" + status +
            ", createdAt=" + createdAt +
            '}';
    }
}
