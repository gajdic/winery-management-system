package cz.muni.fi.pa165.commonservice.api.harvest;

import java.util.UUID;

public class VineyardViewDto {

    private UUID id;
    private String name;
    private String location;
    private double areaSize;
    private String description;

    public VineyardViewDto() {
    }

    public VineyardViewDto(UUID id, String name, String location, double areaSize, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.areaSize = areaSize;
        this.description = description;
    }

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
}
