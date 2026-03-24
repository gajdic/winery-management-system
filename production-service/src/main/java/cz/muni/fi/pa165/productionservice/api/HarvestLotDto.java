package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class HarvestLotDto {
    @NotBlank
    private String grapeType;

    @NotBlank
    private String vineyardLocation;

    @NotNull
    private LocalDate harvestDate;

    @PositiveOrZero
    private double availableKilograms;

    @Min(0)
    @Max(100)
    private int qualityScore;

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
