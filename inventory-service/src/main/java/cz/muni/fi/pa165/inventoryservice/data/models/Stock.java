package cz.muni.fi.pa165.inventoryservice.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "stock")
public class Stock extends EntityBase {

    @Column(nullable = false, unique = true)
    private UUID wineId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity
                && reservedQuantity == stock.reservedQuantity
                && Objects.equals(id, stock.id)
                && Objects.equals(wineId, stock.wineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wineId, quantity, reservedQuantity);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", wineId=" + wineId +
                ", quantity=" + quantity +
                ", reservedQuantity=" + reservedQuantity +
                '}';
    }
}
