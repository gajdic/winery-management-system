package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionPlanningFacade {
    private final ProductionPlanningService productionPlanningService;
    private final HarvestLotMapper harvestLotMapper;
    private final WineRecipeService wineRecipeService;

    @Autowired
    public ProductionPlanningFacade(
        ProductionPlanningService productionPlanningService,
        HarvestLotMapper harvestLotMapper,
        WineRecipeService wineRecipeService
    ) {
        this.productionPlanningService = productionPlanningService;
        this.harvestLotMapper = harvestLotMapper;
        this.wineRecipeService = wineRecipeService;
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
