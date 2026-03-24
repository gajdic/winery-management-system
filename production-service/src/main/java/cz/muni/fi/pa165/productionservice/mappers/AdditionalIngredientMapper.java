package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.AdditionalIngredientDto;
import cz.muni.fi.pa165.productionservice.data.model.AdditionalIngredient;

public class AdditionalIngredientMapper {
    public static AdditionalIngredient toEntity(AdditionalIngredientDto dto) {
        return new AdditionalIngredient(dto.getName(), dto.getQuantityPerLiter(), dto.getUnit());
    }

    public static AdditionalIngredientDto toDto(AdditionalIngredient entity) {
        AdditionalIngredientDto dto = new AdditionalIngredientDto();
        dto.setName(entity.getName());
        dto.setQuantityPerLiter(entity.getQuantityPerLiter());
        dto.setUnit(entity.getUnit());
        return dto;
    }
}
