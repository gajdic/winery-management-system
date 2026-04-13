package cz.muni.fi.pa165.harvestservice.data.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
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
        if (!(o instanceof Vineyard vineyard)) {
            return false;
        }

        return Objects.equals(getId(), vineyard.getId())
            && Objects.equals(getName(), vineyard.getName())
            && Objects.equals(getLocation(), vineyard.getLocation())
            && Double.compare(getAreaSize(), vineyard.getAreaSize()) == 0
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
