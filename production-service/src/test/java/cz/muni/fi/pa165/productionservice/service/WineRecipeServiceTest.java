package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.data.repository.WineRecipeRepository;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WineRecipeServiceTest {

    @Mock
    private WineRecipeRepository wineRecipeRepository;

    @InjectMocks
    private WineRecipeService wineRecipeService;

    @Test
    void create_validRecipe_returnsSavedRecipe() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        Mockito.when(wineRecipeRepository.save(recipe)).thenReturn(recipe);

        WineRecipe result = wineRecipeService.create(recipe);

        assertThat(result).isEqualTo(recipe);
    }

    @Test
    void findById_existingId_returnsRecipe() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.of(recipe));

        WineRecipe result = wineRecipeService.findById(id);

        assertThat(result).isEqualTo(recipe);
    }

    @Test
    void findById_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            wineRecipeService.findById(id);
        });
    }

    @Test
    void findAll_noInput_returnsAllRecipes() {
        List<WineRecipe> recipes = List.of(
            TestDataFactory.wineRecipeEntity("Riesling", 2026),
            TestDataFactory.wineRecipeEntity("Pinot Noir", 2025)
        );

        Mockito.when(wineRecipeRepository.findAll()).thenReturn(recipes);

        List<WineRecipe> result = wineRecipeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void update_existingId_updatesAndReturnsRecipe() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipe existing = TestDataFactory.wineRecipeEntity();
        WineRecipe updatedRecipe = TestDataFactory.wineRecipeEntity("Updated Riesling", 2025);

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.of(existing));

        Mockito.when(wineRecipeRepository.save(existing))
            .thenReturn(existing);

        WineRecipe result = wineRecipeService.update(id, updatedRecipe);

        assertThat(result).isEqualTo(existing);
        assertThat(existing.getName()).isEqualTo("Updated Riesling");
        assertThat(existing.getProductionYear()).isEqualTo(2025);
        assertThat(existing.getAlcoholPercentage()).isEqualTo(12.5);
        assertThat(existing.getTastingNotes()).isEqualTo("Fresh citrus notes");
        assertThat(existing.getBottleVolumeLiters()).isEqualTo(0.75);
    }

    @Test
    void update_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();
        WineRecipe updatedRecipe = TestDataFactory.wineRecipeEntity();

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            wineRecipeService.update(id, updatedRecipe);
        });
    }

    @Test
    void delete_existingId_returnsDeletedRecipe() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.of(recipe));

        WineRecipe result = wineRecipeService.deleteById(id);

        assertThat(result).isEqualTo(recipe);
        Mockito.verify(wineRecipeRepository).delete(recipe);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();

        Mockito.when(wineRecipeRepository.findById(id))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            wineRecipeService.deleteById(id);
        });
        Mockito.verify(wineRecipeRepository, Mockito.never()).delete(Mockito.any());
    }
}
