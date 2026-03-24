package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.mappers.WineRecipeMapper;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public WineRecipeDto createRecipe(WineRecipeCreateDto dto) {
        return wineRecipeMapper.toDto(wineRecipeService.create(wineRecipeMapper.toEntity(dto)));
    }

    public WineRecipeDto findRecipe(Long id) {
        return wineRecipeMapper.toDto(wineRecipeService.findById(id));
    }

    public List<WineRecipeDto> findAllRecipes() {
        return wineRecipeService.findAll().stream().map(wineRecipeMapper::toDto).toList();
    }
}
