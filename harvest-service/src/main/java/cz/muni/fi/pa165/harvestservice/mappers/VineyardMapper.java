package cz.muni.fi.pa165.harvestservice.mappers;

import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VineyardMapper {

    // entity -> view dto
    VineyardViewDto mapToDto(Vineyard vineyard);

    // list mapping
    List<VineyardViewDto> mapToList(List<Vineyard> vineyards);

    // dto -> entity
    Vineyard mapToEntity(VineyardCreateDto dto);

    // update dto -> existing entity
    void updateEntity(VineyardUpdateDto dto, @MappingTarget Vineyard vineyard);
}
