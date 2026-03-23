package cz.muni.fi.pa165.inventoryservice.data.models;

import java.util.UUID;

public class EntityBase {
    protected UUID id = UUID.randomUUID();

    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getId() {
        return id;
    }
}
