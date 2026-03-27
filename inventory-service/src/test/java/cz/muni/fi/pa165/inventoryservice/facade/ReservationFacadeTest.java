package cz.muni.fi.pa165.inventoryservice.facade;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.mappers.ReservationMapper;
import cz.muni.fi.pa165.inventoryservice.service.ReservationService;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Test
    void findById_reservationFound_returnsReservationDetailedViewDto() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity();
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationService.findById(TestDataFactory.RESERVATION_ID)).thenReturn(reservation);
        Mockito.when(reservationMapper.mapToDetailViewDto(reservation)).thenReturn(dto);

        // Act
        ReservationDetailedViewDto result = reservationFacade.findById(TestDataFactory.RESERVATION_ID);

        // Assert
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_reservationNotFound_returnsNull() {
        // Arrange
        Mockito.when(reservationService.findById(TestDataFactory.RESERVATION_ID)).thenReturn(null);

        // Act
        ReservationDetailedViewDto result = reservationFacade.findById(TestDataFactory.RESERVATION_ID);

        // Assert
        assertThat(result).isNull();
        Mockito.verify(reservationMapper, Mockito.never()).mapToDetailViewDto(Mockito.any());
    }

    @Test
    void create_validReservation_returnsCreatedReservationDetailedViewDto() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity();
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationMapper.mapToEntity(dto)).thenReturn(reservation);
        Mockito.when(reservationService.create(reservation)).thenReturn(reservation);
        Mockito.when(reservationMapper.mapToDetailViewDto(reservation)).thenReturn(dto);

        // Act
        ReservationDetailedViewDto result = reservationFacade.create(dto);

        // Assert
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void updateStatus_validStatus_returnsUpdatedReservationDetailedViewDto() {
        // Arrange
        Reservation reservation = TestDataFactory.reservationEntity(ReservationStatus.COMPLETED, 15);
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto(ReservationStatus.COMPLETED, 15);
        Mockito.when(reservationService.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED))
            .thenReturn(reservation);
        Mockito.when(reservationMapper.mapToDetailViewDto(reservation)).thenReturn(dto);

        // Act
        ReservationDetailedViewDto result = reservationFacade.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED);

        // Assert
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void filter_filterArgumentsProvided_returnsReservationBasicViewDtos() {
        // Arrange
        List<Reservation> reservations = List.of(TestDataFactory.reservationEntity());
        List<ReservationBasicViewDto> dtos = TestDataFactory.reservationBasicViewDtos();
        LocalDate createdAfter = LocalDate.of(2025, 2, 1);
        LocalDate createdBefore = LocalDate.of(2025, 4, 1);
        Mockito.when(reservationService.filter(TestDataFactory.CUSTOMER_ID, TestDataFactory.WINE_ID, createdAfter, createdBefore))
            .thenReturn(reservations);
        Mockito.when(reservationMapper.mapToList(reservations)).thenReturn(dtos);

        // Act
        List<ReservationBasicViewDto> result = reservationFacade.filter(TestDataFactory.CUSTOMER_ID, TestDataFactory.WINE_ID, createdAfter, createdBefore);

        // Assert
        assertThat(result).isEqualTo(dtos);
    }
}
