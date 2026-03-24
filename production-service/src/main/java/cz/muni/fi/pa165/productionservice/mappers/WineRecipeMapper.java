package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;

public class WineRecipeMapper {
    public WineRecipe toEntity(WineRecipeCreateDto dto) {
        WineRecipe recipe = new WineRecipe();
        recipe.setName(dto.getName());
        recipe.setProductionYear(dto.getProductionYear());
        recipe.setAlcoholPercentage(dto.getAlcoholPercentage());
        recipe.setTastingNotes(dto.getTastingNotes());
        recipe.setBottleVolumeLiters(dto.getBottleVolumeLiters());
        recipe.setGrapeComponents(dto.getGrapeComponents().stream().map(GrapeComponentMapper::toEntity).toList());
        recipe.setAdditionalIngredients(dto.getAdditionalIngredients().stream().map(AdditionalIngredientMapper::toEntity).toList());
        return recipe;
    }

    public WineRecipeDto toDto(WineRecipe entity) {
        WineRecipeDto dto = new WineRecipeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProductionYear(entity.getProductionYear());
        dto.setAlcoholPercentage(entity.getAlcoholPercentage());
        dto.setTastingNotes(entity.getTastingNotes());
        dto.setBottleVolumeLiters(entity.getBottleVolumeLiters());
        dto.setGrapeComponents(entity.getGrapeComponents().stream().map(GrapeComponentMapper::toDto).toList());
        dto.setAdditionalIngredients(entity.getAdditionalIngredients().stream().map(AdditionalIngredientMapper::toDto).toList());
        return dto;
    }
}
