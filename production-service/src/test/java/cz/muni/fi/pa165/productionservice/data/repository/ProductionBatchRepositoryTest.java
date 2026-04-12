package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductionBatchRepositoryTest {

    @Autowired
    private ProductionBatchRepository productionBatchRepository;

    @Test
    void findByRecipeId_matchingRecipeId_returnsBatches() {
        UUID recipeId = TestDataFactory.RECIPE_ID;
        ProductionBatch matching = TestDataFactory.productionBatchEntity(recipeId, BatchStatus.PLANNED);
        ProductionBatch other = TestDataFactory.productionBatchEntity(UUID.randomUUID(), BatchStatus.PLANNED);
        matching.setId(null);
        other.setId(null);
        productionBatchRepository.saveAll(List.of(matching, other));

        List<ProductionBatch> result = productionBatchRepository.findByRecipeId(recipeId);

        assertThat(result).containsExactly(matching);
    }

    @Test
    void findByStatus_matchingStatus_returnsBatches() {
        ProductionBatch planned = TestDataFactory.productionBatchEntity(UUID.randomUUID(), BatchStatus.PLANNED);
        ProductionBatch completed = TestDataFactory.productionBatchEntity(UUID.randomUUID(), BatchStatus.COMPLETED);
        planned.setId(null);
        completed.setId(null);
        productionBatchRepository.saveAll(List.of(planned, completed));

        List<ProductionBatch> result = productionBatchRepository.findByStatus(BatchStatus.COMPLETED);

        assertThat(result).containsExactly(completed);
    }

}
