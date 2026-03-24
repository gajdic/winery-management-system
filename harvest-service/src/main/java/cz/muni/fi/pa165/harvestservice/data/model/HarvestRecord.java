package cz.muni.fi.pa165.harvestservice.data.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class HarvestRecord {

    private UUID id;
    private GrapeType grapeType;
    private Vineyard vineyard;
    private LocalDate harvestDate;
    private double quantity;
    private double quality;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GrapeType getGrapeType() {
        return grapeType;
    }

    public void setGrapeType(GrapeType grapeType) {
        this.grapeType = grapeType;
    }

    public Vineyard getVineyard() {
        return vineyard;
    }

    public void setVineyard(Vineyard vineyard) {
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

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof HarvestRecord)) {
            return false;
        }

        HarvestRecord harvestRecord = (HarvestRecord) o;

        return getGrapeType().equals(harvestRecord.getGrapeType())
            && getVineyard().equals(harvestRecord.getVineyard())
            && getHarvestDate().equals(harvestRecord.getHarvestDate())
            && getQuantity() == harvestRecord.getQuantity()
            && getQuality() == harvestRecord.getQuality();
    }

    @Override
    public int hashCode() {
        return Objects.hash(grapeType, vineyard, harvestDate, quantity, quality);
    }

    @Override
    public String toString() {
        return "HarvestRecord{"
            + "id=" + id
            + ", grapeType=" + grapeType
            +  ", vineyard=" + vineyard
            + ", harvestDate=" + harvestDate
            + ", quantity=" + quantity
            + ", quality=" + quality
            + "}";
    }
}
