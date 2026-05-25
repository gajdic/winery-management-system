package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Reservation reservation) {
        if (reservation.getId() != null)
            reservation.setId(null);
        return reservationRepository.save(reservation);
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation update(Reservation reservation) {

        if (reservationRepository.findById(reservation.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Reservation not found: " + reservation.getId());
        }
        return reservationRepository.save(reservation);
    }

    public List<Reservation> filter(UUID customerId, UUID wineId, LocalDate createdAfter, LocalDate createdBefore) {
        return reservationRepository.filter(customerId, wineId, createdAfter, createdBefore);
    }

    public void delete(UUID id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found: " + id);
        }
        reservationRepository.deleteById(id);
    }

}
