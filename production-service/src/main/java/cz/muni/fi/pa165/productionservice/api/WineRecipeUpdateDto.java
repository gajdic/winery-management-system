package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class WineRecipeUpdateDto {
    @NotBlank
    private String name;

    @Positive
    private int productionYear;

    @Min(0)
    @Max(100)
    private double alcoholPercentage;

    @NotBlank
    private String tastingNotes;

    @PositiveOrZero
    private double bottleVolumeLiters;

    @Valid
    @NotEmpty
    private List<GrapeComponentDto> grapeComponents;

    @Valid
    private List<AdditionalIngredientDto> additionalIngredients;

    public WineRecipeUpdateDto() {
        this.grapeComponents = new ArrayList<>();
        this.additionalIngredients = new ArrayList<>();
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

    public List<GrapeComponentDto> getGrapeComponents() {
        return grapeComponents;
    }

    public void setGrapeComponents(List<GrapeComponentDto> grapeComponents) {
        this.grapeComponents = grapeComponents;
    }

    public List<AdditionalIngredientDto> getAdditionalIngredients() {
        return additionalIngredients;
    }

    public void setAdditionalIngredients(List<AdditionalIngredientDto> additionalIngredients) {
        this.additionalIngredients = additionalIngredients;
    }
}
