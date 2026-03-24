package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.harvestservice.api.dto.*;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HarvestRecordMapper {

    @Mapping(target = "grapeTypeId", source = "grapeType.id")
    @Mapping(target = "grapeTypeName", source = "grapeType.name")
    @Mapping(target = "vineyardId", source = "vineyard.id")
    @Mapping(target = "vineyardName", source = "vineyard.name")

    // entity -> view dto
    HarvestRecordViewDto mapToDto(HarvestRecord harvestRecord);

    // list mapping
    List<HarvestRecordViewDto> mapToList(List<HarvestRecord> harvestRecords);

    // map to page
//    default Page<HarvestRecordViewDto> mapToPageDto(Page<HarvestRecord> harvestRecords) {
//        return new PageImpl<>(mapToList(harvestRecords.getContent()), harvestRecords.getPageable(), harvestRecords.getTotalPages());
//    }
}
