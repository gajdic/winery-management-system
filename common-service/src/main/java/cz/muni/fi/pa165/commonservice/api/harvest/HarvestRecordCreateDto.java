package cz.muni.fi.pa165.commonservice.api.harvest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public class HarvestRecordCreateDto {

    @NotNull(message = "Grape type cannot be null")
    private UUID grapeTypeId;

    @NotNull(message = "Vineyard cannot be null")
    private UUID vineyardId;

    @NotNull(message = "Harvest date cannot be null")
    @PastOrPresent
    private LocalDate harvestDate;

    @Positive(message = "Quantity must be greater than 0")
    private double quantity;

    @Min(value = 1, message = "Quality must be at least 1")
    @Max(value = 10, message = "Quality must be at most 10")
    private int quality;

    public HarvestRecordCreateDto() {
    }

    public HarvestRecordCreateDto(UUID grapeTypeId, UUID vineyardId, LocalDate harvestDate, double quantity, int quality) {
        this.grapeTypeId = grapeTypeId;
        this.vineyardId = vineyardId;
        this.harvestDate = harvestDate;
        this.quantity = quantity;
        this.quality = quality;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
