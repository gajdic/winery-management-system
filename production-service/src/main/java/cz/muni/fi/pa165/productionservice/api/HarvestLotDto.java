package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public class HarvestLotDto {
    @NotNull
    private UUID grapeTypeId;

    @NotNull
    private UUID vineyardId;

    @NotNull
    @PastOrPresent
    private LocalDate harvestDate;

    @PositiveOrZero
    private double availableKilograms;

    @Min(0)
    @Max(100)
    private int qualityScore;

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
}
