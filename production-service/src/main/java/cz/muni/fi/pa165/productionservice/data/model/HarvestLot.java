package cz.muni.fi.pa165.productionservice.data.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class HarvestLot {
    private UUID grapeTypeId;
    private UUID vineyardId;
    private LocalDate harvestDate;
    private double availableKilograms;
    private int qualityScore;

    public HarvestLot(UUID grapeTypeId, UUID vineyardId, LocalDate harvestDate, double availableKilograms, int qualityScore) {
        this.grapeTypeId = grapeTypeId;
        this.vineyardId = vineyardId;
        this.harvestDate = harvestDate;
        this.availableKilograms = availableKilograms;
        this.qualityScore = qualityScore;
    }

    public UUID getGrapeTypeId() {
        return grapeTypeId;
    }

    public void setGrapeTypeId(UUID grapeTypeId) {
        this.grapeTypeId = grapeTypeId;
    }

    public UUID getVineyardId() {
        return vineyardId;
    }

    public void setVineyardId(UUID vineyardId) {
        this.vineyardId = vineyardId;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public double getAvailableKilograms() {
        return availableKilograms;
    }

    public void setAvailableKilograms(double availableKilograms) {
        this.availableKilograms = availableKilograms;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HarvestLot that = (HarvestLot) o;
        return Double.compare(availableKilograms, that.availableKilograms) == 0 && qualityScore == that.qualityScore && Objects.equals(grapeTypeId, that.grapeTypeId) && Objects.equals(vineyardId, that.vineyardId) && Objects.equals(harvestDate, that.harvestDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grapeTypeId, vineyardId, harvestDate, availableKilograms, qualityScore);
    }

    @Override
    public String toString() {
        return "HarvestLot{" +
            "grapeTypeId=" + grapeTypeId +
            ", vineyardId=" + vineyardId +
            ", harvestDate=" + harvestDate +
            ", availableKilograms=" + availableKilograms +
            ", qualityScore=" + qualityScore +
            '}';
    }
}
