package cz.muni.fi.pa165.harvestservice.data.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
public class Vineyard {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "area_size")
    private double areaSize;

    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vineyard vineyard = (Vineyard) o;

        return Double.compare(vineyard.getAreaSize(), getAreaSize()) == 0
            && Objects.equals(getId(), vineyard.getId())
            && Objects.equals(getName(), vineyard.getName())
            && Objects.equals(getLocation(), vineyard.getLocation())
            && Objects.equals(getDescription(), vineyard.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLocation(), getAreaSize(), getDescription());
    }

    @Override
    public String toString() {
        return "Vineyard {"
            + "id = " + id
            + ", name = '" + name + '\''
            + ", location = '" + location + '\''
            + ", areaSize = " + areaSize
            + ", description = '" + description + '\''
            + "}";
    }
}
