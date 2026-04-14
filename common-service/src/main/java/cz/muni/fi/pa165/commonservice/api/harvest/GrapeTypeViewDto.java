package cz.muni.fi.pa165.commonservice.api.harvest;

import java.util.UUID;

public class GrapeTypeViewDto {
    private UUID id;
    private String name;
    private String description;

    public GrapeTypeViewDto() {
    }

    public GrapeTypeViewDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
