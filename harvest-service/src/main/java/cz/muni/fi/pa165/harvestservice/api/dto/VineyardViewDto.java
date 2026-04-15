package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Detailed view of a vineyard stored in the harvest service")
public class VineyardViewDto {

    @Schema(description = "Unique identifier of the vineyard", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Name of the vineyard", example = "South Hill Vineyard")
    private String name;

    @Schema(description = "Vineyard location or area", example = "Mikulov")
    private String location;

    @Schema(description = "Area size of the vineyard in hectares", example = "12.5")
    private double areaSize;

    @Schema(description = "Optional short description of the vineyard", example = "Sunny south-facing vineyard with limestone soil")
    private String description;

    public VineyardViewDto(UUID id, String name, String location, double areaSize, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.areaSize = areaSize;
        this.description = description;
    }
}
