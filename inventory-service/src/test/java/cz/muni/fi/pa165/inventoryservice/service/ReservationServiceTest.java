package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
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

        // Act + Assert
        assertThatThrownBy(() -> reservationService.findById(TestDataFactory.RESERVATION_ID))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Reservation not found: " + TestDataFactory.RESERVATION_ID);
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
