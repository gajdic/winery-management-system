package cz.muni.fi.pa165.inventoryservice.data.models;

import cz.muni.fi.pa165.commonservice.entity.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "reservation")
public class Reservation extends EntityBase {

    @Column(nullable = false, updatable = false)
    private UUID customerId;

    @Column(nullable = false, updatable = false)
    private UUID wineId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDate createdAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(customerId, that.customerId)
                && Objects.equals(wineId, that.wineId)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, wineId, createdAt);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", wineId=" + wineId +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }
}
