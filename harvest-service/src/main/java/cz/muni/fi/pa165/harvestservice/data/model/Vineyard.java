package cz.muni.fi.pa165.harvestservice.data.model;

import java.util.Objects;
import java.util.UUID;

public class Vineyard {

    private UUID id;
    private String name;
    private String location;
    private double areaSize;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(double areaSize) {
        this.areaSize = areaSize;
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

        if (!(o instanceof Vineyard)) {
            return false;
        }

        Vineyard vineyard = (Vineyard) o;

        return Objects.equals(getName(), vineyard.getName())
            && Objects.equals(getLocation(), vineyard.getLocation())
            && getAreaSize() == vineyard.getAreaSize();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, areaSize);
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
