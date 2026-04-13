package cz.muni.fi.pa165.harvestservice.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
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
        if (!(o instanceof HarvestRecord that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getGrapeType(), that.getGrapeType())
            && Objects.equals(getVineyard(), that.getVineyard())
            && Objects.equals(getHarvestDate(), that.getHarvestDate())
            && Double.compare(getQuantity(), that.getQuantity()) == 0
            && Double.compare(getQuality(), that.getQuality()) == 0;
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
