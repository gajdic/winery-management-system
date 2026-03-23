package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return reservationRepository.create(reservation);
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation update(Reservation reservation) {
        return reservationRepository.update(reservation);
    }

    public boolean delete(UUID id) {
        return reservationRepository.delete(id);
    }
}

