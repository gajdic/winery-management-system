package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void findById_reservationFound_returnsReservation() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity();
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.of(reservation));

        // Act
        Reservation foundReservation = reservationService.findById(TestDataFactory.RESERVATION_ID);

        // Assert
        assertThat(foundReservation).isEqualTo(reservation);
    }

    @Test
    void findById_reservationNotFound_returnsNull() {
        // Arrange
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.empty());

        // Act
        Reservation foundReservation = reservationService.findById(TestDataFactory.RESERVATION_ID);

        // Assert
        assertThat(foundReservation).isNull();
    }

    @Test
    void updateStatus_statusIsNull_throwsIllegalArgumentException() {
        // Act + Assert
        assertThatThrownBy(() -> reservationService.updateStatus(TestDataFactory.RESERVATION_ID, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Status must not be null");
    }

    @Test
    void updateStatus_statusCompleted_updatesStockQuantityAndReservedQuantity() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity(ReservationStatus.COMPLETED, 15);
        Stock stock = TestDataFactory.stockEntity();
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.of(reservation));
        Mockito.when(stockRepository.findByWineId(TestDataFactory.WINE_ID)).thenReturn(Optional.of(stock));

        // Act
        Reservation result = reservationService.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED);

        // Assert
        assertThat(result).isEqualTo(reservation);
        Mockito.verify(stockRepository).findByWineId(TestDataFactory.WINE_ID);
    }

    @Test
    void updateStatus_statusCanceled_updatesReservedQuantityOnly() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity(ReservationStatus.CANCELED, 15);
        Stock stock = TestDataFactory.stockEntity();
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.of(reservation));
        Mockito.when(stockRepository.findByWineId(TestDataFactory.WINE_ID)).thenReturn(Optional.of(stock));

        // Act
        Reservation result = reservationService.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.CANCELED);

        // Assert
        assertThat(result).isEqualTo(reservation);
    }

    @Test
    void updateStatus_statusCreated_doesNotUpdateStock() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity(ReservationStatus.PENDING, 15);
        Stock stock = TestDataFactory.stockEntity();
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.of(reservation));
        Mockito.when(stockRepository.findByWineId(TestDataFactory.WINE_ID)).thenReturn(Optional.of(stock));

        // Act
        Reservation result = reservationService.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.PENDING);

        // Assert
        assertThat(result).isEqualTo(reservation);
        Mockito.verify(stockRepository).findByWineId(TestDataFactory.WINE_ID);
    }

    @Test
    void updateStatus_stockForWineNotFound_throwsIllegalArgumentException() {
        // Arrange
        Mockito.when(reservationRepository.findById(TestDataFactory.RESERVATION_ID)).thenReturn(Optional.of(TestDataFactory.reservationEntity()));
        Mockito.when(stockRepository.findByWineId(TestDataFactory.WINE_ID)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> reservationService.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Stock not found for wine: " + TestDataFactory.WINE_ID);
    }

    @Test
    void filter_filterArgumentsProvided_returnsFilteredReservations() {
        // Arrange
        List<Reservation> reservations = List.of(TestDataFactory.reservationEntity());
        LocalDate createdAfter = LocalDate.of(2025, 2, 1);
        LocalDate createdBefore = LocalDate.of(2025, 4, 1);
        Mockito.when(reservationRepository.filter(TestDataFactory.CUSTOMER_ID, TestDataFactory.WINE_ID, createdAfter, createdBefore))
            .thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.filter(TestDataFactory.CUSTOMER_ID, TestDataFactory.WINE_ID, createdAfter, createdBefore);

        // Assert
        assertThat(result).isEqualTo(reservations);
    }
}
