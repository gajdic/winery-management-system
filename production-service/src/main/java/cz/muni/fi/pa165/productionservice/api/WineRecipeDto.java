package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.*;

public class WineRecipeDto {
    private Long id;

    @NotBlank
    private String name;

    @Positive
    private int productionYear;

    @Min(0)
    @Max(100)
    private double alcoholPercentage;
    private String tastingNotes;

    @PositiveOrZero
    private double bottleVolumeLiters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public String getTastingNotes() {
        return tastingNotes;
    }

    public void setTastingNotes(String tastingNotes) {
        this.tastingNotes = tastingNotes;
    }

    public double getBottleVolumeLiters() {
        return bottleVolumeLiters;
    }

    public void setBottleVolumeLiters(double bottleVolumeLiters) {
        this.bottleVolumeLiters = bottleVolumeLiters;
    }
}
