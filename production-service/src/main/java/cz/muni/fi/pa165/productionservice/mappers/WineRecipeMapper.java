package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WineRecipeMapper {
    WineRecipe mapToEntity(WineRecipeCreateDto dto);

    WineRecipeDto mapToDto(WineRecipe entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(WineRecipeUpdateDto dto, @MappingTarget WineRecipe entity);
}
