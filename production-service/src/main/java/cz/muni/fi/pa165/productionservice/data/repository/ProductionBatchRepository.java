package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductionBatchRepository {
    ProductionBatch save(ProductionBatch batch);

    Optional<ProductionBatch> findById(UUID id);

    List<ProductionBatch> findAll();

    Optional<ProductionBatch> deleteById(UUID id);
}
