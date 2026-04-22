package cz.muni.fi.pa165.inventoryservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Detailed view of a stock entry for a wine item")
public class StockDetailedViewDto {

    @Schema(description = "Unique identifier of the stock entry", example = "770e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Stock id cannot be null")
    private UUID id;

    @Schema(description = "Identifier of the wine the stock refers to", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Wine id cannot be null")
    private UUID wineId;

    @Schema(description = "Total quantity currently held in stock", example = "120")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

    @Schema(description = "Portion of the stock quantity that is currently reserved", example = "25")
    @Min(value = 0, message = "Reserved quantity must be at least 0")
    private int reservedQuantity;

}
