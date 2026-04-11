package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.AdditionalIngredient;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WineRecipeRepositoryTest {

    @Autowired
    private WineRecipeRepository wineRecipeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByNameContainingIgnoreCase_matchingName_returnsRecipes() {
        WineRecipe riesling = createRecipe("Late Harvest Riesling", 2025);
        WineRecipe pinot = createRecipe("Pinot Noir", 2024);
        wineRecipeRepository.saveAll(List.of(riesling, pinot));

        List<WineRecipe> result = wineRecipeRepository.findByNameContainingIgnoreCase("riesling");

        assertThat(result).containsExactly(riesling);
    }

    @Test
    void findByProductionYear_matchingYear_returnsRecipes() {
        WineRecipe current = createRecipe("Sauvignon Blanc", 2025);
        WineRecipe older = createRecipe("Cabernet Moravia", 2024);
        wineRecipeRepository.saveAll(List.of(current, older));

        List<WineRecipe> result = wineRecipeRepository.findByProductionYear(2025);

        assertThat(result).containsExactly(current);
    }

    @Test
    void save_recipeWithComponents_persistsEmbeddedCollections() {
        WineRecipe recipe = createRecipe("Cuvee Reserve", 2026);

        WineRecipe saved = wineRecipeRepository.saveAndFlush(recipe);
        entityManager.clear();

        WineRecipe result = wineRecipeRepository.findById(saved.getId()).orElseThrow();

        assertThat(result.getGrapeComponents()).containsExactly(new GrapeComponent("Riesling", 1.3, 85));
        assertThat(result.getAdditionalIngredients()).containsExactly(new AdditionalIngredient("Yeast", 0.02, "kg"));
    }

    private static WineRecipe createRecipe(String name, int productionYear) {
        WineRecipe recipe = new WineRecipe();
        recipe.setName(name);
        recipe.setProductionYear(productionYear);
        recipe.setAlcoholPercentage(12.5);
        recipe.setTastingNotes("Fresh citrus notes");
        recipe.setBottleVolumeLiters(0.75);
        recipe.setGrapeComponents(List.of(new GrapeComponent("Riesling", 1.3, 85)));
        recipe.setAdditionalIngredients(List.of(new AdditionalIngredient("Yeast", 0.02, "kg")));
        return recipe;
    }
}
