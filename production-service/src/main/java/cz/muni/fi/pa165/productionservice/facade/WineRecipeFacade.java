package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.*;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.mappers.WineRecipeMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WineRecipeFacade {
    private final WineRecipeService wineRecipeService;
    private final WineRecipeMapper wineRecipeMapper;
    private final ProductionPlanningService productionPlanningService;
    private final HarvestLotMapper harvestLotMapper;

    @Autowired
    public WineRecipeFacade(
        WineRecipeService wineRecipeService,
        WineRecipeMapper wineRecipeMapper,
        ProductionPlanningService productionPlanningService,
        HarvestLotMapper harvestLotMapper
    ) {
        this.wineRecipeService = wineRecipeService;
        this.wineRecipeMapper = wineRecipeMapper;
        this.productionPlanningService = productionPlanningService;
        this.harvestLotMapper = harvestLotMapper;
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

    public ProductionPlanDto planProduction(ProductionPlanRequestDto dto) {
        WineRecipe recipe = wineRecipeService.findById(dto.getRecipeId());
        return productionPlanningService.createProductionPlan(recipe, dto.getDesiredBottleCount(), harvestLotMapper.toHarvestEntities(dto.getAvailableHarvests()));
    }

    public QualityEvaluationDto evaluateQuality(QualityEvaluationRequestDto dto) {
        WineRecipe recipe = wineRecipeService.findById(dto.getRecipeId());
        return productionPlanningService.evaluateBlendQuality(recipe, harvestLotMapper.toHarvestEntities(dto.getHarvestLots()));
    }
}
