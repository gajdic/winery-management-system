package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.mappers.WineRecipeMapper;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class WineRecipeFacade {
    private final WineRecipeService wineRecipeService;
    private final WineRecipeMapper wineRecipeMapper;

    @Autowired
    public WineRecipeFacade(
        WineRecipeService wineRecipeService,
        WineRecipeMapper wineRecipeMapper
    ) {
        this.wineRecipeService = wineRecipeService;
        this.wineRecipeMapper = wineRecipeMapper;
    }

    @Transactional
    public WineRecipeDto createRecipe(WineRecipeCreateDto dto) {
        return wineRecipeMapper.mapToDto(wineRecipeService.create(wineRecipeMapper.mapToEntity(dto)));
    }

    public WineRecipeDto findRecipe(UUID id) {
        return wineRecipeMapper.mapToDto(wineRecipeService.findById(id));
    }

    public List<WineRecipeDto> findAllRecipes() {
        return wineRecipeService.findAll().stream().map(wineRecipeMapper::mapToDto).toList();
    }

    @Transactional
    public WineRecipeDto updateRecipe(UUID id, WineRecipeUpdateDto dto) {
        return wineRecipeMapper.mapToDto(wineRecipeService.update(id, wineRecipeMapper.mapToEntity(dto)));
    }

    @Transactional
    public void deleteRecipe(UUID id) {
        wineRecipeService.deleteById(id);
    }
}
