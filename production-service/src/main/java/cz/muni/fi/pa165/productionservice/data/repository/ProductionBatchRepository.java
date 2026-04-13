package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, UUID> {
    List<ProductionBatch> findByRecipeId(UUID recipeId);

    List<ProductionBatch> findByStatus(BatchStatus status);
}
