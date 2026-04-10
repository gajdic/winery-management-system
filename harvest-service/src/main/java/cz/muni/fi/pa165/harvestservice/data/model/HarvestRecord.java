package cz.muni.fi.pa165.harvestservice.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
public class HarvestRecord {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "grape_type_id", nullable = false)
    private GrapeType grapeType;

    @ManyToOne
    @JoinColumn(name = "vineyard_id", nullable = false)
    private Vineyard vineyard;

    @Column(name = "harvest_date")
    private LocalDate harvestDate;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "quality")
    private double quality;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HarvestRecord harvestRecord = (HarvestRecord) o;

        return Double.compare(harvestRecord.getQuantity(), getQuantity()) == 0
            && Double.compare(harvestRecord.getQuality(), getQuality()) == 0
            && Objects.equals(getId(), harvestRecord.getId())
            && Objects.equals(getGrapeType(), harvestRecord.getGrapeType())
            && Objects.equals(getVineyard(), harvestRecord.getVineyard())
            && Objects.equals(getHarvestDate(), harvestRecord.getHarvestDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGrapeType(), getVineyard(), getHarvestDate(), getQuantity(), getQuality());
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
