package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class GrapeComponentDto {
    @NotBlank
    private String grapeType;

    @PositiveOrZero
    private double kilogramsPerLiter;

    @Min(0)
    @Max(100)
    private int preferredQuality;

    public String getGrapeType() {
        return grapeType;
    }

    public void setGrapeType(String grapeType) {
        this.grapeType = grapeType;
    }

    public double getKilogramsPerLiter() {
        return kilogramsPerLiter;
    }

    public void setKilogramsPerLiter(double kilogramsPerLiter) {
        this.kilogramsPerLiter = kilogramsPerLiter;
    }

    public int getPreferredQuality() {
        return preferredQuality;
    }

    public void setPreferredQuality(int preferredQuality) {
        this.preferredQuality = preferredQuality;
    }
}
