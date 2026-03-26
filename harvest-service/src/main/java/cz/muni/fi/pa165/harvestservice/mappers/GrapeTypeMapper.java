package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrapeTypeMapper {

    // entity -> view dto
    GrapeTypeViewDto mapToDto(GrapeType grapeType);

    // list mapping
    List<GrapeTypeViewDto> mapToList(List<GrapeType> grapeTypes);

    // dto -> entity
    @Mapping(target = "id", ignore = true)
    GrapeType mapToEntity(GrapeTypeCreateDto dto);

    // id + update dto -> entity
    @Mapping(target = "id", source = "id")
    GrapeType mapToEntity(java.util.UUID id, GrapeTypeUpdateDto dto);

    // update dto -> existing entity
    @Mapping(target = "id", ignore = true)
    void updateEntity(GrapeTypeUpdateDto dto, @MappingTarget GrapeType grapeType);
}
