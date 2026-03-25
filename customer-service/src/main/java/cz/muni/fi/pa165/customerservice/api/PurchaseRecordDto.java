package cz.muni.fi.pa165.customerservice.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class PurchaseRecordDto {

    @NotNull
    private UUID id;

    @NotNull
    private UUID customerId;

    @NotNull
    private UUID wineId;

    @Min(1)
    private int quantity;

    @Min(0)
    private double pricePerBottle;

    @DateTimeFormat
    private LocalDateTime purchaseDate;

    // getters and setters

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

}
