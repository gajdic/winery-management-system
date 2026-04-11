package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ReservationRepository extends InMemoryRepository<Reservation> {

    public Reservation updateStatus(UUID id, ReservationStatus status) {
        Reservation reservation = super.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
        reservation.setStatus(status);
        return super.update(reservation);
    }

    public List<Reservation> filter(UUID customerId, UUID wineId, LocalDate createdAfter, LocalDate createdBefore) {
        return storage.values().stream()
            .filter(reservation -> customerId == null || customerId.equals(reservation.getCustomerId()))
            .filter(reservation -> wineId == null || wineId.equals(reservation.getWineId()))
            .filter(reservation -> createdAfter == null || reservation.getCreatedAt().isAfter(createdAfter))
            .filter(reservation -> createdBefore == null || reservation.getCreatedAt().isBefore(createdBefore))
            .toList();
    }
}
