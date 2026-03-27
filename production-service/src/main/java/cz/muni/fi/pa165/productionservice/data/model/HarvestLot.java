package cz.muni.fi.pa165.productionservice.data.model;

import java.time.LocalDate;
import java.util.Objects;

public class HarvestLot {
    private String grapeType;
    private String vineyardLocation;
    private LocalDate harvestDate;
    private double availableKilograms;
    private int qualityScore;

    public HarvestLot(String grapeType, String vineyardLocation, LocalDate harvestDate, double availableKilograms, int qualityScore) {
        this.grapeType = grapeType;
        this.vineyardLocation = vineyardLocation;
        this.harvestDate = harvestDate;
        this.availableKilograms = availableKilograms;
        this.qualityScore = qualityScore;
    }

    public String getGrapeType() {
        return grapeType;
    }

    public void setGrapeType(String grapeType) {
        this.grapeType = grapeType;
    }

    public String getVineyardLocation() {
        return vineyardLocation;
    }

    public void setVineyardLocation(String vineyardLocation) {
        this.vineyardLocation = vineyardLocation;
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
        return Double.compare(availableKilograms, that.availableKilograms) == 0 && qualityScore == that.qualityScore && Objects.equals(grapeType, that.grapeType) && Objects.equals(vineyardLocation, that.vineyardLocation) && Objects.equals(harvestDate, that.harvestDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grapeType, vineyardLocation, harvestDate, availableKilograms, qualityScore);
    }

    @Override
    public String toString() {
        return "HarvestLot{" +
            "grapeType='" + grapeType + '\'' +
            ", vineyardLocation='" + vineyardLocation + '\'' +
            ", harvestDate=" + harvestDate +
            ", availableKilograms=" + availableKilograms +
            ", qualityScore=" + qualityScore +
            '}';
    }
}
