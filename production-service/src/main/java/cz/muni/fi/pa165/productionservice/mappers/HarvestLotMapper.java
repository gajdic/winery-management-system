package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HarvestLotMapper {
    List<HarvestLot> toHarvestEntities(List<HarvestLotDto> dtos);
}
