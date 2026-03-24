package cz.muni.fi.pa165.productionservice.data.model;

import java.time.LocalDate;

public class HarvestLot {
    private String grapeType;
    private String vineyardLocation;
    private LocalDate harvestDate;
    private double availableKilograms;
    private int qualityScore;

    public HarvestLot() {
    }

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
}
