package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Required grape component of a wine recipe.")
public class GrapeComponentDto {
    @NotBlank
    @Schema(description = "Grape variety required by the recipe.", example = "Riesling")
    private String grapeType;

    @Positive
    @Schema(description = "Kilograms of grapes needed per produced liter.", example = "1.35", minimum = "0", exclusiveMinimum = true)
    private double kilogramsPerLiter;

    @Min(0)
    @Max(100)
    @Schema(description = "Preferred quality score for this grape component.", example = "85", minimum = "0", maximum = "100")
    private int preferredQuality;
}
