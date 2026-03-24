package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionBatchRepository {
    ProductionBatch save(ProductionBatch batch);

    Optional<ProductionBatch> findById(Long id);

    List<ProductionBatch> findAll();
}
