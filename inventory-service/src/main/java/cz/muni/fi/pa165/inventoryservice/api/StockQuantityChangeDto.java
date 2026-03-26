package cz.muni.fi.pa165.inventoryservice.api;

import jakarta.validation.constraints.Min;

public class StockQuantityChangeDto {
    @Min(0)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

