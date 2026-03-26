package cz.muni.fi.pa165.customerservice.data.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class PurchaseRecord {
    private UUID id;
    private UUID customerId;
    private UUID wineId;
    private int quantity;
    private double pricePerBottle;
    private LocalDateTime purchaseDate;

    public PurchaseRecord(){

    }

    public PurchaseRecord(UUID id, UUID customerId, UUID wineId, int quantity, double pricePerBottle, LocalDateTime purchaseDate) {
        this.id = id;
        this.customerId = customerId;
        this.wineId = wineId;
        this.quantity = quantity;
        this.pricePerBottle = pricePerBottle;
        this.purchaseDate = purchaseDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public double getPricePerBottle() {
        return pricePerBottle;
    }

    public void setPricePerBottle(double pricePerBottle) {
        this.pricePerBottle = pricePerBottle;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseRecord that = (PurchaseRecord) o;
        return quantity == that.quantity && Double.compare(pricePerBottle, that.pricePerBottle) == 0 && Objects.equals(id, that.id) && Objects.equals(customerId, that.customerId) && Objects.equals(wineId, that.wineId) && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, wineId, quantity, pricePerBottle, purchaseDate);
    }

    @Override
    public String toString() {
        return "PurchaseRecord{" +
            "id=" + id +
            ", customerId=" + customerId +
            ", wineId=" + wineId +
            ", quantity=" + quantity +
            ", pricePerBottle=" + pricePerBottle +
            ", purchaseDate=" + purchaseDate +
            '}';
    }
}
