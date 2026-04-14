package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordViewDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface HarvestRecordMapper {

    @Mapping(target = "grapeType.id", source = "grapeType.id")
    @Mapping(target = "grapeType.name", source = "grapeType.name")
    @Mapping(target = "vineyard.id", source = "vineyard.id")
    @Mapping(target = "vineyard.name", source = "vineyard.name")
    HarvestRecordViewDto mapToDto(HarvestRecord harvestRecord);

    List<HarvestRecordViewDto> mapToList(List<HarvestRecord> harvestRecords);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "grapeType.id", source = "grapeTypeId")
    @Mapping(target = "vineyard.id", source = "vineyardId")
    HarvestRecord mapToEntity(HarvestRecordCreateDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "grapeType.id", source = "dto.grapeTypeId")
    @Mapping(target = "vineyard.id", source = "dto.vineyardId")
    HarvestRecord mapToEntity(UUID id, HarvestRecordUpdateDto dto);

    default List<HarvestSummaryDto> mapSummaryToDtoList(Map<String, Double> summary) {
        if (summary == null) {
            return List.of();
        }
        return summary.entrySet().stream()
            .map(entry -> new HarvestSummaryDto(entry.getKey(), entry.getValue()))
            .toList();
    }
}
