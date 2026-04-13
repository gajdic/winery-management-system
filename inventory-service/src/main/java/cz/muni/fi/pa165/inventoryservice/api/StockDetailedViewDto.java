package cz.muni.fi.pa165.inventoryservice.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class StockDetailedViewDto {
    @NotNull
    private UUID id;

    @NotNull
    private UUID wineId;

    @Min(0)
    private int quantity;

    @Min(0)
    private int reservedQuantity;

}
