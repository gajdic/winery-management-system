package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductionBatchRepositoryTest {

    @Autowired
    private ProductionBatchRepository productionBatchRepository;

    @Test
    void findByRecipeId_matchingRecipeId_returnsBatches() {
        UUID recipeId = UUID.randomUUID();
        ProductionBatch matching = createBatch(recipeId, BatchStatus.PLANNED);
        ProductionBatch other = createBatch(UUID.randomUUID(), BatchStatus.PLANNED);
        productionBatchRepository.saveAll(List.of(matching, other));

        List<ProductionBatch> result = productionBatchRepository.findByRecipeId(recipeId);

        assertThat(result).containsExactly(matching);
    }

    @Test
    void findByStatus_matchingStatus_returnsBatches() {
        ProductionBatch planned = createBatch(UUID.randomUUID(), BatchStatus.PLANNED);
        ProductionBatch completed = createBatch(UUID.randomUUID(), BatchStatus.COMPLETED);
        productionBatchRepository.saveAll(List.of(planned, completed));

        List<ProductionBatch> result = productionBatchRepository.findByStatus(BatchStatus.COMPLETED);

        assertThat(result).containsExactly(completed);
    }

    private static ProductionBatch createBatch(UUID recipeId, BatchStatus status) {
        ProductionBatch batch = new ProductionBatch();
        batch.setRecipeId(recipeId);
        batch.setPlannedLiters(75.0);
        batch.setPlannedBottleCount(100);
        batch.setActualBottleCount(0);
        batch.setStatus(status);
        batch.setCreatedAt(LocalDateTime.of(2026, 4, 11, 12, 0));
        return batch;
    }
}
