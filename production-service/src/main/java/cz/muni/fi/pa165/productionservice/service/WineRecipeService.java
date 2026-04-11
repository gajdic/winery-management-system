package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.data.repository.WineRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WineRecipeService {
    private final WineRecipeRepository wineRecipeRepository;

    @Autowired
    public WineRecipeService(WineRecipeRepository wineRecipeRepository) {
        this.wineRecipeRepository = wineRecipeRepository;
    }

    public WineRecipe create(WineRecipe wineRecipe) {
        return wineRecipeRepository.save(wineRecipe);
    }

    public WineRecipe findById(UUID id) {
        return wineRecipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wine recipe with id " + id + " not found"));
    }

    public List<WineRecipe> findAll() {
        return wineRecipeRepository.findAll();
    }

    public WineRecipe update(UUID id, WineRecipe updatedRecipe) {
        WineRecipe existingRecipe = wineRecipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wine recipe with id " + id + " not found"));

        existingRecipe.setName(updatedRecipe.getName());
        existingRecipe.setProductionYear(updatedRecipe.getProductionYear());
        existingRecipe.setAlcoholPercentage(updatedRecipe.getAlcoholPercentage());
        existingRecipe.setTastingNotes(updatedRecipe.getTastingNotes());
        existingRecipe.setBottleVolumeLiters(updatedRecipe.getBottleVolumeLiters());
        existingRecipe.setGrapeComponents(updatedRecipe.getGrapeComponents());
        existingRecipe.setAdditionalIngredients(updatedRecipe.getAdditionalIngredients());

        return wineRecipeRepository.save(existingRecipe);
    }

    public WineRecipe deleteById(UUID id) {
        WineRecipe recipe = findById(id);
        wineRecipeRepository.delete(recipe);
        return recipe;
    }
}
