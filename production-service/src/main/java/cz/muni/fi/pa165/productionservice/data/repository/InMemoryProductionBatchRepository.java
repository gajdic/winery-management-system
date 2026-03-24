package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductionBatchRepository implements ProductionBatchRepository {
    private final AtomicLong idSequence = new AtomicLong(1);
    private final ConcurrentMap<UUID, ProductionBatch> batches = new ConcurrentHashMap<>();

    @Override
    public ProductionBatch save(ProductionBatch batch) {
        if (batch.getId() == null) {
            batch.setId(idSequence.getAndIncrement());
        }
        batches.put(batch.getId(), batch);
        return batch;
    }

    @Override
    public Optional<ProductionBatch> findById(UUID id) {
        return Optional.ofNullable(batches.get(id));
    }

    @Override
    public List<ProductionBatch> findAll() {
        return new ArrayList<>(batches.values());
    }
}

