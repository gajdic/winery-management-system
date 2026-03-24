package cz.muni.fi.pa165.harvestservice.data.model;

import java.util.Objects;
import java.util.UUID;

public class GrapeType {

    private UUID id;
    private String name;
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrapeType)) {
            return false;
        }

        GrapeType grapeType = (GrapeType) o;

        return Objects.equals(getName(), grapeType.getName())
            && Objects.equals(getDescription(), grapeType.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
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
