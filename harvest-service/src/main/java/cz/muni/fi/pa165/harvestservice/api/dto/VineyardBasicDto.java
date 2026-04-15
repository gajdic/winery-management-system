package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Compact vineyard reference used inside nested DTOs")
public class VineyardBasicDto {

    @Schema(description = "Unique identifier of the vineyard", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Name of the vineyard", example = "South Hill Vineyard")
    private String name;

    public VineyardBasicDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
