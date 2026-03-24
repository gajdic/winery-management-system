package cz.muni.fi.pa165.inventoryservice.data.models;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Reservation extends EntityBase {
    private UUID customerId;
    private UUID wineId;
    private int quantity;
    private ReservationStatus status;
    private LocalDate createdAt;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

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

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

