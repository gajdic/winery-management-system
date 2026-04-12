package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WineRecipeMapper {
    @Mapping(target = "id", ignore = true)
    WineRecipe mapToEntity(WineRecipeCreateDto dto);

    @Mapping(target = "id", ignore = true)
    WineRecipe mapToEntity(WineRecipeUpdateDto dto);

    WineRecipeDto mapToDto(WineRecipe entity);
}
