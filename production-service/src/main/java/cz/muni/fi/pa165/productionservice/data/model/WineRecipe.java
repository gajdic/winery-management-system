package cz.muni.fi.pa165.productionservice.data.model;

import java.util.ArrayList;
import java.util.List;

public class WineRecipe {
    private Long id;
    private String name;
    private int productionYear;
    private double alcoholPercentage;
    private String tastingNotes;
    private double bottleVolumeLiters;

    private List<GrapeComponent> grapeComponents;
    private List<AdditionalIngredient> additionalIngredients;

    public WineRecipe() {
        grapeComponents = new ArrayList<>();
        additionalIngredients = new ArrayList<>();
    }

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

    public List<GrapeComponent> getGrapeComponents() {
        return grapeComponents;
    }

    public void setGrapeComponents(List<GrapeComponent> grapeComponents) {
        this.grapeComponents = grapeComponents;
    }

    public List<AdditionalIngredient> getAdditionalIngredients() {
        return additionalIngredients;
    }

    public void setAdditionalIngredients(List<AdditionalIngredient> additionalIngredients) {
        this.additionalIngredients = additionalIngredients;
    }
}
