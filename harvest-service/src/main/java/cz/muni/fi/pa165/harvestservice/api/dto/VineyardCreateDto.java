package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Request payload used to create a new vineyard")
public class VineyardCreateDto {

    @Schema(description = "Name of the vineyard", example = "South Hill Vineyard")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "Vineyard location or area", example = "Mikulov")
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Schema(description = "Area size of the vineyard in hectares", example = "12.5")
    @Positive(message = "Area size must be greater than 0")
    private double areaSize;

    @Schema(description = "Optional short description of the vineyard", example = "Sunny south-facing vineyard with limestone soil")
    private String description;

    public VineyardCreateDto(String name, String location, double areaSize, String description) {
        this.name = name;
        this.location = location;
        this.areaSize = areaSize;
        this.description = description;
    }
}
