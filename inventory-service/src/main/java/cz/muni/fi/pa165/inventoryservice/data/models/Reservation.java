package cz.muni.fi.pa165.inventoryservice.data.models;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;

import java.util.Date;

public class Reservation extends EntityBase {
    private Long customerId;
    private Long wineId;
    private int quantity;
    private ReservationStatus status;
    private Date createdAt;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getWineId() {
        return wineId;
    }

    public void setWineId(Long wineId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

