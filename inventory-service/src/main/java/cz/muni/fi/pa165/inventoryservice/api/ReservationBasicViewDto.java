package cz.muni.fi.pa165.inventoryservice.api;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class ReservationBasicViewDto {
    @NotNull
    private UUID id;
    private ReservationStatus status;
    private LocalDate createdAt;

}
