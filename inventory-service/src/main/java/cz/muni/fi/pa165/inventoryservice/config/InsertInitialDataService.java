package cz.muni.fi.pa165.inventoryservice.config;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class InsertInitialDataService {

    private final StockRepository stockRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public InsertInitialDataService(StockRepository stockRepository, ReservationRepository reservationRepository) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    public void insertDummyData() {
        List<Stock> stocks = insertStocks();
        insertReservations(stocks);
    }

    private List<Stock> insertStocks() {
        Stock cabernet = createStock(UUID.randomUUID(), 180);
        Stock merlot = createStock(UUID.randomUUID(), 140);
        Stock riesling = createStock(UUID.randomUUID(), 90);
        return List.of(cabernet, merlot, riesling);
    }

    private void insertReservations(List<Stock> stocks) {
        if (stocks.isEmpty()) {
            return;
        }

        createReservation(stocks.get(0), 14, ReservationStatus.PENDING, LocalDate.now().minusDays(3));
        createReservation(stocks.get(1), 8, ReservationStatus.PENDING, LocalDate.now().minusDays(1));
        createReservation(stocks.get(0), 6, ReservationStatus.COMPLETED, LocalDate.now().minusDays(7));
    }

    private Stock createStock(UUID wineId, int quantity) {
        Stock stock = new Stock();
        stock.setWineId(wineId);
        stock.setQuantity(quantity);
        stock.setReservedQuantity(0);
        return stockRepository.save(stock);
    }

    private void createReservation(Stock stock, int quantity, ReservationStatus status, LocalDate createdAt) {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(stock.getWineId());
        reservation.setQuantity(quantity);
        reservation.setStatus(status);
        reservation.setCreatedAt(createdAt);
        reservationRepository.save(reservation);
        applyReservationToStock(stock, reservation);
    }

    private void applyReservationToStock(Stock stock, Reservation reservation) {
        switch (reservation.getStatus()) {
            case PENDING -> {
                stock.setReservedQuantity(stock.getReservedQuantity() + reservation.getQuantity());
                stockRepository.save(stock);
            }
            case COMPLETED -> {
                stock.setQuantity(stock.getQuantity() - reservation.getQuantity());
                stockRepository.save(stock);
            }
            case CANCELED -> { }
        }
    }
}
