package cz.muni.fi.pa165.inventoryservice.controller;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.facade.ReservationFacade;
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
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @PostMapping
    public ResponseEntity<ReservationDetailedViewDto> create(@RequestBody Reservation reservation) {
        ReservationDetailedViewDto created = reservationFacade.create(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailedViewDto> findById(@PathVariable UUID id) {
        ReservationDetailedViewDto reservation = reservationFacade.findById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationBasicViewDto>> findAll() {
        return ResponseEntity.ok(reservationFacade.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservationBasicViewDto>> filter(
        @RequestParam(required = false) UUID customerId,
        @RequestParam(required = false) UUID wineId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAfter,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore) {
        return ResponseEntity.ok(reservationFacade.filter(customerId, wineId, createdAfter, createdBefore));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDetailedViewDto> update(@PathVariable UUID id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        ReservationDetailedViewDto updated = reservationFacade.update(reservation);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationDetailedViewDto> updateStatus(@PathVariable UUID id, @RequestParam ReservationStatus status) {
        try {
            ReservationDetailedViewDto updated = reservationFacade.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = reservationFacade.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
