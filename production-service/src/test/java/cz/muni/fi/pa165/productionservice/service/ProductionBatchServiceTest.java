package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.data.repository.ProductionBatchRepository;
import cz.muni.fi.pa165.productionservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductionBatchServiceTest {

    @Mock
    private ProductionBatchRepository productionBatchRepository;

    @InjectMocks
    private ProductionBatchService productionBatchService;

    @Test
    void createPlannedBatch_validInput_createsBatchWithCorrectValues() {
        UUID recipeId = UUID.randomUUID();

        ProductionPlanDto plan = new ProductionPlanDto();
        plan.setFeasibleBottleCount(100);
        plan.setProducibleLiters(75.0);

        Mockito.when(productionBatchRepository.save(Mockito.any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ProductionBatch result = productionBatchService.createPlannedBatch(recipeId, plan);

        assertThat(result.getRecipeId()).isEqualTo(recipeId);
        assertThat(result.getPlannedBottleCount()).isEqualTo(100);
        assertThat(result.getPlannedLiters()).isEqualTo(75.0);
        assertThat(result.getActualBottleCount()).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(BatchStatus.PLANNED);
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_existingId_returnsBatch() {
        UUID id = UUID.randomUUID();
        ProductionBatch batch = new ProductionBatch();

        Mockito.when(productionBatchRepository.findById(id))
            .thenReturn(Optional.of(batch));

        ProductionBatch result = productionBatchService.findById(id);

        assertThat(result).isEqualTo(batch);
    }

    @Test
    void findById_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();

        Mockito.when(productionBatchRepository.findById(id))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productionBatchService.findById(id);
        });
    }

    @Test
    void findAll_noInput_returnsAllBathes() {
        List<ProductionBatch> batches = List.of(new ProductionBatch(), new ProductionBatch());

        Mockito.when(productionBatchRepository.findAll()).thenReturn(batches);

        List<ProductionBatch> result = productionBatchService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void updateStatus_existingId_updatesStatus() {
        UUID id = UUID.randomUUID();
        ProductionBatch batch = new ProductionBatch();

        Mockito.when(productionBatchRepository.findById(id))
            .thenReturn(Optional.of(batch));

        Mockito.when(productionBatchRepository.save(batch))
            .thenReturn(batch);

        ProductionBatch result = productionBatchService.updateStatus(id, BatchStatus.COMPLETED);

        assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    void delete_existingId_returnsDeletedBatch() {
        UUID id = UUID.randomUUID();
        ProductionBatch batch = new ProductionBatch();

        Mockito.when(productionBatchRepository.deleteById(id))
            .thenReturn(Optional.of(batch));

        ProductionBatch result = productionBatchService.deleteById(id);

        assertThat(result).isEqualTo(batch);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        UUID id = UUID.randomUUID();

        Mockito.when(productionBatchRepository.deleteById(id))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productionBatchService.deleteById(id);
        });
    }
}
