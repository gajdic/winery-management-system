package cz.muni.fi.pa165.productionservice.data.model;

import java.util.Objects;

public class AdditionalIngredient {
    private String name;
    private double quantityPerLiter;
    private String unit;

    public AdditionalIngredient(String name, double quantityPerLiter, String unit) {
        this.name = name;
        this.quantityPerLiter = quantityPerLiter;
        this.unit = unit;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AdditionalIngredient that = (AdditionalIngredient) o;
        return Double.compare(quantityPerLiter, that.quantityPerLiter) == 0 && Objects.equals(name, that.name) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantityPerLiter, unit);
    }

    @Override
    public String toString() {
        return "AdditionalIngredient{" +
            "name='" + name + '\'' +
            ", quantityPerLiter=" + quantityPerLiter +
            ", unit='" + unit + '\'' +
            '}';
    }
}
