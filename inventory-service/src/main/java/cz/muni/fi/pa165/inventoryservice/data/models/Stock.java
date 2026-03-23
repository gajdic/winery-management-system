package cz.muni.fi.pa165.inventoryservice.data.models;

import java.util.UUID;

public class Stock extends EntityBase {
    private UUID wineId;
    private int quantity;
    private int reservedQuantity;


    public UUID getWineId() {
        return wineId;
    }

    public void setWineId(UUID wineId) {
        this.wineId = wineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }
}

