package cz.muni.fi.pa165.inventoryservice.api;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;

import java.util.Date;
import java.util.UUID;

public class ReservationBasicViewDto {
    private UUID id;
    private ReservationStatus status;
    private Date createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
