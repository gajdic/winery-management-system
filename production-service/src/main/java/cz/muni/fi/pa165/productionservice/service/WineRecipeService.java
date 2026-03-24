package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.data.repository.WineRecipeRepository;
import cz.muni.fi.pa165.productionservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.productionservice.mappers.WineRecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WineRecipeService {
    private final WineRecipeRepository wineRecipeRepository;
    private final WineRecipeMapper wineRecipeMapper;

    @Autowired
    public WineRecipeService(WineRecipeRepository wineRecipeRepository, WineRecipeMapper wineRecipeMapper) {
        this.wineRecipeRepository = wineRecipeRepository;
        this.wineRecipeMapper = wineRecipeMapper;
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

    public WineRecipe update(UUID id, WineRecipeUpdateDto dto) {
        WineRecipe existingRecipe = wineRecipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wine recipe with id " + id + " not found"));

        wineRecipeMapper.updateEntityFromDto(dto, existingRecipe);
        return wineRecipeRepository.save(existingRecipe);
    }

    public WineRecipe deleteById(UUID id) {
        return wineRecipeRepository.deleteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wine recipe with id " + id + " not found"));
    }
}
