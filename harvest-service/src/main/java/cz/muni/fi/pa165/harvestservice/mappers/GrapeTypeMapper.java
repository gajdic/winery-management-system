package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrapeTypeMapper {

    // entity -> view dto
    GrapeTypeViewDto mapToDto(GrapeType grapeType);

    // list mapping
    List<GrapeTypeViewDto> mapToList(List<GrapeType> grapeTypes);

    // dto -> entity
    GrapeType mapToEntity(GrapeTypeCreateDto dto);

    // update dto -> existing entity
    void updateEntity(GrapeTypeUpdateDto dto, @MappingTarget GrapeType grapeType);

    // map to page
    default Page<GrapeTypeViewDto> mapToPageDto(Page<GrapeType> grapeTypes) {
        return new PageImpl<>(mapToList(grapeTypes.getContent()), grapeTypes.getPageable(), grapeTypes.getTotalPages());
    }
}
