package cz.muni.fi.pa165.commonservice.api.harvest;

import jakarta.validation.constraints.NotBlank;

public class GrapeTypeCreateDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;

    public GrapeTypeCreateDto() {
    }

    public GrapeTypeCreateDto(String name, String description) {
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
