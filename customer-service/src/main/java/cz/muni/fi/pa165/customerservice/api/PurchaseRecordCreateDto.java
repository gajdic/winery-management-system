package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Body for creating a new purchase record")
public class PurchaseRecordCreateDto {

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
}
