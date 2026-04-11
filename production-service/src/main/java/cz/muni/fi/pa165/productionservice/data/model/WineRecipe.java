package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wine_recipes")
public class WineRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private int productionYear;
    private double alcoholPercentage;
    @Column(length = 1000)
    private String tastingNotes;
    private double bottleVolumeLiters;

    @ElementCollection
    @CollectionTable(
        name = "wine_recipe_grape_components",
        joinColumns = @JoinColumn(name = "wine_recipe_id")
    )
    private List<GrapeComponent> grapeComponents;

    @ElementCollection
    @CollectionTable(
        name = "wine_recipe_additional_ingredients",
        joinColumns = @JoinColumn(name = "wine_recipe_id")
    )
    private List<AdditionalIngredient> additionalIngredients;

    public WineRecipe() {
        grapeComponents = new ArrayList<>();
        additionalIngredients = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WineRecipe that = (WineRecipe) o;
        return productionYear == that.productionYear && Double.compare(alcoholPercentage, that.alcoholPercentage) == 0 && Double.compare(bottleVolumeLiters, that.bottleVolumeLiters) == 0 && Objects.equals(name, that.name) && Objects.equals(tastingNotes, that.tastingNotes) && Objects.equals(grapeComponents, that.grapeComponents) && Objects.equals(additionalIngredients, that.additionalIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, productionYear, alcoholPercentage, tastingNotes, bottleVolumeLiters, grapeComponents, additionalIngredients);
    }

    @Override
    public String toString() {
        return "WineRecipe{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", productionYear=" + productionYear +
            ", alcoholPercentage=" + alcoholPercentage +
            ", tastingNotes='" + tastingNotes + '\'' +
            ", bottleVolumeLiters=" + bottleVolumeLiters +
            ", grapeComponents=" + grapeComponents +
            ", additionalIngredients=" + additionalIngredients +
            '}';
    }
}
