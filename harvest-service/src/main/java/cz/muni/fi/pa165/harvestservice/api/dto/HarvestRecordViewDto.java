package cz.muni.fi.pa165.harvestservice.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public class HarvestRecordViewDto {

    private UUID id;
    private UUID grapeTypeId;
    private String grapeTypeName;
    private UUID vineyardId;
    private String vineyardName;
    private LocalDate harvestDate;
    private double quantity;
    private int quality;

    public HarvestRecordViewDto() {
    }

    public HarvestRecordViewDto(UUID id, UUID grapeTypeId, String grapeTypeName, UUID vineyardId, String vineyardName, LocalDate harvestDate, double quantity, int quality) {
        this.id = id;
        this.grapeTypeId = grapeTypeId;
        this.grapeTypeName = grapeTypeName;
        this.vineyardId = vineyardId;
        this.vineyardName = vineyardName;
        this.harvestDate = harvestDate;
        this.quantity = quantity;
        this.quality = quality;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGrapeTypeId() {
        return grapeTypeId;
    }

    public void setGrapeTypeId(UUID grapeTypeId) {
        this.grapeTypeId = grapeTypeId;
    }

    public String getGrapeTypeName() {
        return grapeTypeName;
    }

    public void setGrapeTypeName(String grapeTypeName) {
        this.grapeTypeName = grapeTypeName;
    }

    public UUID getVineyardId() {
        return vineyardId;
    }

    public void setVineyardId(UUID vineyardId) {
        this.vineyardId = vineyardId;
    }

    public String getVineyardName() {
        return vineyardName;
    }

    public void setVineyardName(String vineyardName) {
        this.vineyardName = vineyardName;
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
