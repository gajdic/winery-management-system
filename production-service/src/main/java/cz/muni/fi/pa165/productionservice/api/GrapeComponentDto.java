package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GrapeComponentDto {
    @NotBlank
    private String grapeType;

    @Positive
    private double kilogramsPerLiter;

    @Min(0)
    @Max(100)
    private int preferredQuality;
}
