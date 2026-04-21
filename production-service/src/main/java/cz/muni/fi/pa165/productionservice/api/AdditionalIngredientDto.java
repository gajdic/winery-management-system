package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AdditionalIngredientDto {
    @NotBlank
    private String name;

    @PositiveOrZero
    private double quantityPerLiter;

    @NotBlank
    private String unit;
}
