package cz.muni.fi.pa165.inventoryservice.api;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockQuantityChangeDto {
    @Min(0)
    private int quantity;

}

