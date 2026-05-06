package cz.muni.fi.pa165.inventoryservice.controller;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.config.SecurityConfig;
import cz.muni.fi.pa165.inventoryservice.facade.ReservationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "Endpoints for managing wine stock reservations")
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create a reservation",
        description = "Creates a reservation of a given wine quantity for a customer",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid reservation payload")
    })
    public ResponseEntity<ReservationDetailedViewDto> create(@RequestBody @Valid ReservationDetailedViewDto reservation) {
        ReservationDetailedViewDto created = reservationFacade.create(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a reservation by id",
        description = "Returns one reservation with detailed customer and wine references",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation found"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDetailedViewDto> findById(@PathVariable UUID id) {
        ReservationDetailedViewDto reservation = reservationFacade.findById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    @Operation(
        summary = "List all reservations",
        description = "Returns all reservations currently registered in the inventory service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservations returned successfully")
    })
    public ResponseEntity<List<ReservationBasicViewDto>> findAll() {
        return ResponseEntity.ok(reservationFacade.findAll());
    }

    @GetMapping("/filter")
    @Operation(
        summary = "Filter reservations",
        description = "Returns reservations filtered by customer, wine and creation date range",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservations returned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
    })
    public ResponseEntity<List<ReservationBasicViewDto>> filter(
        @RequestParam(required = false) UUID customerId,
        @RequestParam(required = false) UUID wineId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAfter,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore) {
        return ResponseEntity.ok(reservationFacade.filter(customerId, wineId, createdAfter, createdBefore));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a reservation",
        description = "Updates an existing reservation by its identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid reservation payload"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDetailedViewDto> update(@PathVariable UUID id, @RequestBody @Valid ReservationDetailedViewDto reservation) {
        reservation.setId(id);
        ReservationDetailedViewDto updated = reservationFacade.update(reservation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a reservation",
        description = "Removes a reservation from the inventory service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reservation deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = reservationFacade.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
