package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionPlanningFacade {
    private final ProductionPlanningService productionPlanningService;
    private final HarvestLotMapper harvestLotMapper;

    @Autowired
    public ProductionPlanningFacade(
        ProductionPlanningService productionPlanningService,
        HarvestLotMapper harvestLotMapper
    ) {
        this.productionPlanningService = productionPlanningService;
        this.harvestLotMapper = harvestLotMapper;
    }

    public ProductionPlanDto planProduction(ProductionPlanRequestDto dto) {
        return productionPlanningService.createProductionPlan(
            dto.getRecipeId(),
            dto.getDesiredBottleCount(),
            harvestLotMapper.toHarvestEntities(dto.getAvailableHarvests())
        );
    }

    public QualityEvaluationDto evaluateQuality(QualityEvaluationRequestDto dto) {
        return productionPlanningService.evaluateBlendQuality(
            dto.getRecipeId(),
            harvestLotMapper.toHarvestEntities(dto.getHarvestLots())
        );
    }
}
