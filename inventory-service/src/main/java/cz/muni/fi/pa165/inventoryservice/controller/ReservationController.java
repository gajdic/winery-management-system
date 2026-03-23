package cz.muni.fi.pa165.inventoryservice.controller;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.facade.ReservationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDetailedViewDto> update(@PathVariable UUID id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        ReservationDetailedViewDto updated = reservationFacade.update(reservation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = reservationFacade.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

