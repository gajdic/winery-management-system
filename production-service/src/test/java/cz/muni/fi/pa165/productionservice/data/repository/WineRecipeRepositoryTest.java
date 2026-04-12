package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
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
        WineRecipe riesling = TestDataFactory.wineRecipeEntity("Late Harvest Riesling", 2025);
        WineRecipe pinot = TestDataFactory.wineRecipeEntity("Pinot Noir", 2024);
        riesling.setId(null);
        pinot.setId(null);
        wineRecipeRepository.saveAll(List.of(riesling, pinot));

        List<WineRecipe> result = wineRecipeRepository.findByNameContainingIgnoreCase("riesling");

        assertThat(result).containsExactly(riesling);
    }

    @Test
    void findByNameContainingIgnoreCase_noMatchingName_returnsEmptyList() {
        WineRecipe riesling = TestDataFactory.wineRecipeEntity("Late Harvest Riesling", 2025);
        riesling.setId(null);
        wineRecipeRepository.save(riesling);

        List<WineRecipe> result = wineRecipeRepository.findByNameContainingIgnoreCase("pinot");

        assertThat(result).isEmpty();
    }

    @Test
    void findByProductionYear_matchingYear_returnsRecipes() {
        WineRecipe current = TestDataFactory.wineRecipeEntity("Sauvignon Blanc", 2025);
        WineRecipe older = TestDataFactory.wineRecipeEntity("Cabernet Moravia", 2024);
        current.setId(null);
        older.setId(null);
        wineRecipeRepository.saveAll(List.of(current, older));

        List<WineRecipe> result = wineRecipeRepository.findByProductionYear(2025);

        assertThat(result).containsExactly(current);
    }

    @Test
    void findByProductionYear_noMatchingYear_returnsEmptyList() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity("Sauvignon Blanc", 2025);
        recipe.setId(null);
        wineRecipeRepository.save(recipe);

        List<WineRecipe> result = wineRecipeRepository.findByProductionYear(2024);

        assertThat(result).isEmpty();
    }

    @Test
    void save_recipeWithComponents_persistsEmbeddedCollections() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity("Cuvee Reserve", 2026);
        recipe.setId(null);

        WineRecipe saved = wineRecipeRepository.saveAndFlush(recipe);
        entityManager.clear();

        WineRecipe result = wineRecipeRepository.findById(saved.getId()).orElseThrow();

        assertThat(result.getGrapeComponents()).containsExactly(TestDataFactory.grapeComponent());
        assertThat(result.getAdditionalIngredients()).containsExactly(TestDataFactory.additionalIngredient());
    }
}
