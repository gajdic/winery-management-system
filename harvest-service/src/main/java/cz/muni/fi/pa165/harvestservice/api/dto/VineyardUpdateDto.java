package cz.muni.fi.pa165.harvestservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class VineyardUpdateDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Positive(message = "Area size must be greater than 0")
    private double areaSize;

    private String description;

    public VineyardUpdateDto() {
    }

    public VineyardUpdateDto(String name, String location, double areaSize, String description) {
        this.name = name;
        this.location = location;
        this.areaSize = areaSize;
        this.description = description;
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
