package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.data.repository.ProductionBatchRepository;
import cz.muni.fi.pa165.productionservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductionBatchService {
    private final ProductionBatchRepository productionBatchRepository;

    @Autowired
    public ProductionBatchService(ProductionBatchRepository productionBatchRepository) {
        this.productionBatchRepository = productionBatchRepository;
    }

    public ProductionBatch createPlannedBatch(UUID recipeId, ProductionPlanDto plan) {
        ProductionBatch batch = new ProductionBatch();
        batch.setRecipeId(recipeId);
        batch.setPlannedBottleCount(plan.getFeasibleBottleCount());
        batch.setPlannedLiters(plan.getProducibleLiters());
        batch.setActualBottleCount(0);
        batch.setStatus(BatchStatus.PLANNED);
        batch.setCreatedAt(LocalDateTime.now());
        return productionBatchRepository.save(batch);
    }

    public ProductionBatch findById(UUID id) {
        return productionBatchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production batch with id " + id + " was not found."));
    }

    public List<ProductionBatch> findAll() {
        return productionBatchRepository.findAll();
    }

    public ProductionBatch updateStatus(UUID id, BatchStatus status) {
        ProductionBatch batch = findById(id);
        batch.setStatus(status);
        return productionBatchRepository.save(batch);
    }
}
