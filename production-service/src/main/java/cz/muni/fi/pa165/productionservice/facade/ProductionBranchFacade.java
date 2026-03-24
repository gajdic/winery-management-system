package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.mappers.ProductionBatchMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionBatchService;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductionBranchFacade {
    private final ProductionBatchService productionBatchService;
    private final ProductionBatchMapper productionBatchMapper;
    private final WineRecipeService wineRecipeService;
    private final ProductionPlanningService productionPlanningService;
    private final HarvestLotMapper harvestLotMapper;

    @Autowired
    public ProductionBranchFacade(
        ProductionBatchService productionBatchService,
        ProductionBatchMapper productionBatchMapper,
        WineRecipeService wineRecipeService,
        ProductionPlanningService productionPlanningService,
        HarvestLotMapper harvestLotMapper
    ) {
        this.productionBatchService = productionBatchService;
        this.productionBatchMapper = productionBatchMapper;
        this.wineRecipeService = wineRecipeService;
        this.productionPlanningService = productionPlanningService;
        this.harvestLotMapper = harvestLotMapper;
    }

    public ProductionBatchDto createBatch(ProductionBatchCreateDto dto, List<HarvestLotDto> harvestLots) {
        WineRecipe recipe = wineRecipeService.findById(dto.getRecipeId());
        ProductionPlanDto plan = productionPlanningService.createProductionPlan(recipe, dto.getDesiredBottleCount(), harvestLotMapper.toHarvestEntities(harvestLots));
        return productionBatchMapper.toDto(productionBatchService.createPlannedBatch(dto.getRecipeId(), plan));
    }

    public List<ProductionBatchDto> findAllBatches() {
        return productionBatchService.findAll().stream().map(productionBatchMapper::toDto).toList();
    }

    public ProductionBatchDto findBatch(UUID id) {
        return productionBatchMapper.toDto(productionBatchService.findById(id));
    }

    public ProductionBatchDto updateBatchStatus(UUID id, BatchStatus status) {
        return productionBatchMapper.toDto(productionBatchService.updateStatus(id, status));
    }
}
