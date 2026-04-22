package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(description = "Additional ingredient used in a wine recipe.")
public class AdditionalIngredientDto {
    @NotBlank
    @Schema(description = "Ingredient name.", example = "Yeast")
    private String name;

    @PositiveOrZero
    @Schema(description = "Ingredient quantity needed per produced liter.", example = "0.02", minimum = "0")
    private double quantityPerLiter;

    @NotBlank
    @Schema(description = "Measurement unit for the ingredient quantity.", example = "g")
    private String unit;
}
