package cz.muni.fi.pa165.harvestservice.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public class HarvestRecordViewDto {

    private UUID id;
    private GrapeTypeBasicDto grapeType;
    private VineyardBasicDto vineyard;
    private LocalDate harvestDate;
    private double quantity;
    private int quality;

    public HarvestRecordViewDto() {
    }

    public HarvestRecordViewDto( UUID id, GrapeTypeBasicDto grapeType,VineyardBasicDto vineyard, LocalDate harvestDate, double quantity, int quality) {
        this.id = id;
        this.grapeType = grapeType;
        this.vineyard = vineyard;
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

    public GrapeTypeBasicDto getGrapeType() {
        return grapeType;
    }

    public void setGrapeType(GrapeTypeBasicDto grapeType) {
        this.grapeType = grapeType;
    }

    public VineyardBasicDto getVineyard() {
        return vineyard;
    }

    public void setVineyard(VineyardBasicDto vineyard) {
        this.vineyard = vineyard;
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
