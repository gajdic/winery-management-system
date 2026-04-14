package cz.muni.fi.pa165.inventoryservice.api;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationDetailedViewDto extends ReservationBasicViewDto {
    @NotNull
    private UUID wineId;
    @NotNull
    private UUID customerId;
    @Min(0)
    private int quantity;


}
