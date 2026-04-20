package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Information about an existing purchase record")
public class PurchaseRecordDto {

    @NotNull
    @Schema(description = "Unique identifier of the purchase record", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID id;

    @NotNull
    @Schema(description = "Id of the customer") //TODO
    private UUID customerId;

    @NotNull
    @Schema(description = "Id of the wine") //TODO
    private UUID wineId;

    @Min(1)
    @Schema(description = "The amount of bottles bought", example = "7")
    private int quantity;

    @Min(0)
    @Schema(description = "Price per one bottle bought", example = "8.9")
    private double pricePerBottle;

    @DateTimeFormat
    @PastOrPresent
    @Schema(description = "Purchase date of the order") //TODO example
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
