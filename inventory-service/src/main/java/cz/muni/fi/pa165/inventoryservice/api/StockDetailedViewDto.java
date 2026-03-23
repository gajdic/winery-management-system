package cz.muni.fi.pa165.inventoryservice.api;

import java.util.UUID;

public class StockDetailedViewDto {
    private UUID id;
    private UUID wineId;
    private int quantity;
    private int reservedQuantity;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWineId() {
        return wineId;
    }

    public void setWineId(UUID wineId) {
        this.wineId = wineId;
    }
}
