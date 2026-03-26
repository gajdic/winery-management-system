package cz.muni.fi.pa165.inventoryservice.mappers;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation mapToEntity(ReservationDetailedViewDto reservation);

    ReservationBasicViewDto mapToDto(Reservation reservation);

    ReservationDetailedViewDto mapToDetailViewDto(Reservation reservation);

    List<ReservationBasicViewDto> mapToList(List<Reservation> reservations);

    default Page<ReservationBasicViewDto> mapToPageDto(Page<Reservation> reservations) {
        return new PageImpl<>(mapToList(reservations.getContent()), reservations.getPageable(), reservations.getTotalPages());
    }
}
