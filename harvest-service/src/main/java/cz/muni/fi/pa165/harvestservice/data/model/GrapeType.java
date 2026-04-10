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
public class GrapeType {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

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

        GrapeType grapeType = (GrapeType) o;

        return Objects.equals(getId(), grapeType.getId())
            && Objects.equals(getName(), grapeType.getName())
            && Objects.equals(getDescription(), grapeType.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "GrapeType {"
            + "id = " + id
            + ", name = '" + name + '\''
            + ", description = '" + description + '\''
            + '}';
    }
}
