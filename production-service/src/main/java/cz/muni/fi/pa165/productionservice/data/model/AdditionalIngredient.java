package cz.muni.fi.pa165.productionservice.data.model;

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
}
