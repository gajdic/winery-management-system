package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Request payload used to create a new grape type")
public class GrapeTypeCreateDto {

    @Schema(description = "Business name of the grape variety", example = "Riesling")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "Optional short description of the grape variety", example = "Aromatic white grape suitable for cooler climates")
    private String description;

    public GrapeTypeCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
