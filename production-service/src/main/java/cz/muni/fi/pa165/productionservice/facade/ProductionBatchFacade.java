package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.mappers.ProductionBatchMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionBatchService;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductionBatchFacade {
    private final ProductionBatchService productionBatchService;
    private final ProductionBatchMapper productionBatchMapper;
    private final ProductionPlanningService productionPlanningService;
    private final HarvestLotMapper harvestLotMapper;

    @Autowired
    public ProductionBatchFacade(
        ProductionBatchService productionBatchService,
        ProductionBatchMapper productionBatchMapper,
        ProductionPlanningService productionPlanningService,
        HarvestLotMapper harvestLotMapper
    ) {
        this.productionBatchService = productionBatchService;
        this.productionBatchMapper = productionBatchMapper;
        this.productionPlanningService = productionPlanningService;
        this.harvestLotMapper = harvestLotMapper;
    }

    public ProductionBatchDto createBatch(ProductionBatchCreateDto dto, List<HarvestLotDto> harvestLots) {
        ProductionPlanDto plan = productionPlanningService.createProductionPlan(
            dto.getRecipeId(),
            dto.getDesiredBottleCount(),
            harvestLotMapper.toHarvestEntities(harvestLots)
        );
        return productionBatchMapper.mapToDto(productionBatchService.createPlannedBatch(dto.getRecipeId(), plan));
    }

    public List<ProductionBatchDto> findAllBatches() {
        return productionBatchService.findAll().stream().map(productionBatchMapper::mapToDto).toList();
    }

    public ProductionBatchDto findBatch(UUID id) {
        return productionBatchMapper.mapToDto(productionBatchService.findById(id));
    }

    public ProductionBatchDto updateBatchStatus(UUID id, BatchStatus status) {
        return productionBatchMapper.mapToDto(productionBatchService.updateStatus(id, status));
    }

    public void deleteBatch(UUID id) {
        productionBatchMapper.mapToDto(productionBatchService.deleteById(id));
    }
}
