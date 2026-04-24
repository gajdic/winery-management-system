package cz.muni.fi.pa165.inventoryservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Basic view of a reservation with identification and lifecycle metadata")
public class ReservationBasicViewDto {

    @Schema(description = "Unique identifier of the reservation", example = "880e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Reservation id cannot be null")
    private UUID id;

    @Schema(description = "Date when the reservation was created", example = "2025-10-15")
    private LocalDate createdAt;

}
