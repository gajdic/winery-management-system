package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Compact grape type reference used inside nested DTOs")
public class GrapeTypeBasicDto {

    @Schema(description = "Unique identifier of the grape type", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Business name of the grape variety", example = "Riesling")
    private String name;

    public GrapeTypeBasicDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
