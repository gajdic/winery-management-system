package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wine_recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
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
    private List<GrapeComponent> grapeComponents = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "wine_recipe_additional_ingredients",
        joinColumns = @JoinColumn(name = "wine_recipe_id")
    )
    private List<AdditionalIngredient> additionalIngredients = new ArrayList<>();
}
