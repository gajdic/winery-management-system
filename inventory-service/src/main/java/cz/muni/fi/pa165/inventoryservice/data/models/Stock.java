package cz.muni.fi.pa165.inventoryservice.data.models;

import cz.muni.fi.pa165.commonservice.entity.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "stock")
public class Stock extends EntityBase {

    @Column(nullable = false, unique = true, updatable = false)
    private UUID wineId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int reservedQuantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(wineId, stock.wineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wineId);
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
