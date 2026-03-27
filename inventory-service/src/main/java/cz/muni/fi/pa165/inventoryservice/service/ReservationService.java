package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, StockRepository stockRepository) {
        this.reservationRepository = reservationRepository;
        this.stockRepository = stockRepository;
    }

    public Reservation create(Reservation reservation) {
        return reservationRepository.create(reservation);
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation update(Reservation reservation) {
        return reservationRepository.update(reservation);
    }

    public Reservation updateStatus(UUID id, ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        var reservation = reservationRepository.updateStatus(id, status);

        var stockId = stockRepository.findByWine(reservation.getWineId())
            .orElseThrow(() -> new IllegalArgumentException("Stock not found for wine: " + reservation.getWineId()))
            .getId();

        switch (reservation.getStatus()) {
            case COMPLETED -> {
                stockRepository.updateQuantity(stockId, -reservation.getQuantity());
                stockRepository.updateReservedQuantity(stockId, -reservation.getQuantity());
            }
            case CANCELED -> {
                stockRepository.updateReservedQuantity(stockId, -reservation.getQuantity());
            }
        }

        return reservation;
    }

    public List<Reservation> filter(UUID customerId, UUID wineId, LocalDate createdAfter, LocalDate createdBefore) {
        return reservationRepository.filter(customerId, wineId, createdAfter, createdBefore);
    }

    public boolean delete(UUID id) {
        return reservationRepository.delete(id);
    }

}
