package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.GrapeComponentDto;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;

public class GrapeComponentMapper {
    public static GrapeComponent toEntity(GrapeComponentDto dto) {
        return new GrapeComponent(dto.getGrapeType(), dto.getKilogramsPerLiter(), dto.getPreferredQuality());
    }

    public static GrapeComponentDto toDto(GrapeComponent entity) {
        GrapeComponentDto dto = new GrapeComponentDto();
        dto.setGrapeType(entity.getGrapeType());
        dto.setKilogramsPerLiter(entity.getKilogramsPerLiter());
        dto.setPreferredQuality(entity.getPreferredQuality());
        return dto;
    }
}
