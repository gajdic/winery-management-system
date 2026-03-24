package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;

import java.util.List;

public class HarvestLotMapper {
    public List<HarvestLot> toHarvestEntities(List<HarvestLotDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    private HarvestLot toEntity(HarvestLotDto dto) {
        return new HarvestLot(dto.getGrapeType(), dto.getVineyardLocation(), dto.getHarvestDate(), dto.getAvailableKilograms(), dto.getQualityScore());
    }
}
