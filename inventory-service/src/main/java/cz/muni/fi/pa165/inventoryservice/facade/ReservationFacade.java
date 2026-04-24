package cz.muni.fi.pa165.inventoryservice.facade;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
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

    public ReservationDetailedViewDto create(ReservationDetailedViewDto reservation) {
        return reservationMapper.mapToDetailViewDto(reservationService.create(reservationMapper.mapToEntity(reservation)));
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

    public ReservationDetailedViewDto update(ReservationDetailedViewDto reservation) {
        return reservationMapper.mapToDetailViewDto(reservationService.update(reservationMapper.mapToEntity(reservation)));
    }

    public boolean delete(UUID id) {
        return reservationService.delete(id);
    }
}
