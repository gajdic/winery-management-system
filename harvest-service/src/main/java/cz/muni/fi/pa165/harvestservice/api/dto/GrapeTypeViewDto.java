package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Detailed view of a grape type stored in the harvest service")
public class GrapeTypeViewDto {

    @Schema(description = "Unique identifier of the grape type", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Business name of the grape variety", example = "Riesling")
    private String name;

    @Schema(description = "Optional short description of the grape variety", example = "Aromatic white grape suitable for cooler climates")
    private String description;

    public GrapeTypeViewDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
