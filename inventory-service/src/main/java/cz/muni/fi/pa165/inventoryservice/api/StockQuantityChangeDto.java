package cz.muni.fi.pa165.inventoryservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Request payload used to add or remove a quantity from a stock entry")
public class StockQuantityChangeDto {

    @Schema(description = "Quantity to add to or remove from the stock", example = "10")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

}
