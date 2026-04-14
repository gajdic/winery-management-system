package cz.muni.fi.pa165.commonservice.api.harvest;

import java.util.UUID;

public class VineyardBasicDto {

    private UUID id;
    private String name;

    public VineyardBasicDto() {
    }

    public VineyardBasicDto(UUID id, String name) {
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
