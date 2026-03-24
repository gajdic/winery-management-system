package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface HarvestRecordMapper {

    @Mapping(target = "grapeTypeId", source = "grapeType.id")
    @Mapping(target = "grapeTypeName", source = "grapeType.name")
    @Mapping(target = "vineyardId", source = "vineyard.id")
    @Mapping(target = "vineyardName", source = "vineyard.name")
    HarvestRecordViewDto mapToDto(HarvestRecord harvestRecord);

    List<HarvestRecordViewDto> mapToList(List<HarvestRecord> harvestRecords);

    default HarvestRecord mapToEntity(HarvestRecordCreateDto dto) {
        if (dto == null) {
            return null;
        }

        HarvestRecord harvestRecord = new HarvestRecord();
        harvestRecord.setGrapeType(mapGrapeType(dto.getGrapeTypeId()));
        harvestRecord.setVineyard(mapVineyard(dto.getVineyardId()));
        harvestRecord.setHarvestDate(dto.getHarvestDate());
        harvestRecord.setQuantity(dto.getQuantity());
        harvestRecord.setQuality(dto.getQuality());

        return harvestRecord;
    }

    default HarvestRecord mapToEntity(UUID id, HarvestRecordUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        HarvestRecord harvestRecord = new HarvestRecord();
        harvestRecord.setId(id);
        harvestRecord.setGrapeType(mapGrapeType(dto.getGrapeTypeId()));
        harvestRecord.setVineyard(mapVineyard(dto.getVineyardId()));
        harvestRecord.setHarvestDate(dto.getHarvestDate());
        harvestRecord.setQuantity(dto.getQuantity());
        harvestRecord.setQuality(dto.getQuality());

        return harvestRecord;
    }

    default GrapeType mapGrapeType(UUID id) {
        if (id == null) {
            return null;
        }

        GrapeType grapeType = new GrapeType();
        grapeType.setId(id);
        return grapeType;
    }

    default Vineyard mapVineyard(UUID id) {
        if (id == null) {
            return null;
        }

        Vineyard vineyard = new Vineyard();
        vineyard.setId(id);
        return vineyard;
    }
}
