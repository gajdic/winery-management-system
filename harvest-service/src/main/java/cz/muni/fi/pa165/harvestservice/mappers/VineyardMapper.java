package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.commonservice.api.harvest.VineyardCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VineyardMapper {

    // entity -> view dto
    VineyardViewDto mapToDto(Vineyard vineyard);

    // list mapping
    List<VineyardViewDto> mapToList(List<Vineyard> vineyards);

    // dto -> entity
    @Mapping(target = "id", ignore = true)
    Vineyard mapToEntity(VineyardCreateDto dto);

    // id + update dto -> entity
    @Mapping(target = "id", source = "id")
    Vineyard mapToEntity(java.util.UUID id, VineyardUpdateDto dto);

    // update dto -> existing entity
    @Mapping(target = "id", ignore = true)
    void updateEntity(VineyardUpdateDto dto, @MappingTarget Vineyard vineyard);
}
