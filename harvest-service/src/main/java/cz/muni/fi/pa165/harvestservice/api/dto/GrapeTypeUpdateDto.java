package cz.muni.fi.pa165.harvestservice.api.dto;

import jakarta.validation.constraints.NotBlank;

public class GrapeTypeUpdateDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;

    public GrapeTypeUpdateDto() {
    }

    public GrapeTypeUpdateDto(String name, String description) {
        this.name = name;
        this.description = description;
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
}
