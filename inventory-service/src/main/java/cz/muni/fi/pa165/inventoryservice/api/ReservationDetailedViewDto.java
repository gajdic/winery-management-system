package cz.muni.fi.pa165.inventoryservice.api;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Detailed view of a reservation including customer, wine and reserved quantity")
public class ReservationDetailedViewDto extends ReservationBasicViewDto {

    @Schema(description = "Identifier of the reserved wine", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Wine id cannot be null")
    private UUID wineId;

    @Schema(description = "Identifier of the customer who owns the reservation", example = "990e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Customer id cannot be null")
    private UUID customerId;

    @Schema(description = "Quantity of wine reserved for the customer", example = "6")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

}
