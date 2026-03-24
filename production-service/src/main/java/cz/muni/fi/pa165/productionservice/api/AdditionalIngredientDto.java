package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class AdditionalIngredientDto {
    @NotBlank
    private String name;

    @PositiveOrZero
    private double quantityPerLiter;

    @NotBlank
    private String unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantityPerLiter() {
        return quantityPerLiter;
    }

    public void setQuantityPerLiter(double quantityPerLiter) {
        this.quantityPerLiter = quantityPerLiter;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
