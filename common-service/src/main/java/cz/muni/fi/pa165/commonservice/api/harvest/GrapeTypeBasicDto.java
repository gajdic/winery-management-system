package cz.muni.fi.pa165.commonservice.api.harvest;

import java.util.UUID;

public class GrapeTypeBasicDto {

    private UUID id;
    private String name;

    public GrapeTypeBasicDto() {
    }

    public GrapeTypeBasicDto(UUID id, String name) {
        this.id = id;
        this.name = name;
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
}
