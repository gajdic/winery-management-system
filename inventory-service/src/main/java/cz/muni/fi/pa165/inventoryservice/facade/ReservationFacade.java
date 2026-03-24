package cz.muni.fi.pa165.inventoryservice.facade;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.mappers.ReservationMapper;
import cz.muni.fi.pa165.inventoryservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ReservationFacade {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationFacade(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    public ReservationDetailedViewDto create(Reservation reservation) {
        return reservationMapper.mapToDetailViewDto(reservationService.create(reservation));
    }

    public ReservationDetailedViewDto findById(UUID id) {
        var reservation = reservationService.findById(id);
        return reservation == null ? null : reservationMapper.mapToDetailViewDto(reservation);
    }

    public List<ReservationBasicViewDto> findAll() {
        return reservationMapper.mapToList(reservationService.findAll());
    }

    public List<ReservationBasicViewDto> filter(UUID customerId, UUID wineId, LocalDate createdAfter, LocalDate createdBefore) {
        return reservationMapper.mapToList(reservationService.filter(customerId, wineId, createdAfter, createdBefore));
    }

    public ReservationDetailedViewDto update(Reservation reservation) {
        return reservationMapper.mapToDetailViewDto(reservationService.update(reservation));
    }

    public ReservationDetailedViewDto updateStatus(UUID id, ReservationStatus status) {
        return reservationMapper.mapToDetailViewDto(reservationService.updateStatus(id, status));
    }

    public boolean delete(UUID id) {
        return reservationService.delete(id);
    }
}
