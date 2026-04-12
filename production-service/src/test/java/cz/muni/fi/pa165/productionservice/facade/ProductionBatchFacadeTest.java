package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.mappers.ProductionBatchMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionBatchService;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductionBatchFacadeTest {

    @Mock
    private ProductionBatchService productionBatchService;

    @Mock
    private ProductionBatchMapper productionBatchMapper;

    @Mock
    private ProductionPlanningService productionPlanningService;

    @Mock
    private HarvestLotMapper harvestLotMapper;

    @InjectMocks
    private ProductionBatchFacade productionBatchFacade;

    @Test
    void createBatch_validRequest_returnsDto() {
        UUID recipeId = TestDataFactory.RECIPE_ID;

        ProductionBatchCreateDto dto = TestDataFactory.productionBatchCreateDto();

        ProductionPlanDto plan = TestDataFactory.productionPlanDto();

        List<HarvestLotDto> harvestDtos = List.of(TestDataFactory.harvestLotDto());
        List<HarvestLot> harvestEntities = List.of(TestDataFactory.harvestLot());

        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        ProductionBatchDto batchDto = TestDataFactory.productionBatchDto();

        Mockito.when(harvestLotMapper.toHarvestEntities(harvestDtos))
            .thenReturn(harvestEntities);

        Mockito.when(productionPlanningService.createProductionPlan(
            recipeId,
            dto.getDesiredBottleCount(),
            harvestEntities
        )).thenReturn(plan);

        Mockito.when(productionBatchService.createPlannedBatch(recipeId, plan))
            .thenReturn(batch);

        Mockito.when(productionBatchMapper.mapToDto(batch))
            .thenReturn(batchDto);

        ProductionBatchDto result =
            productionBatchFacade.createBatch(dto, harvestDtos);

        assertThat(result).isEqualTo(batchDto);
    }

    @Test
    void findAllBatches_noInput_returnsMappedList() {
        ProductionBatch b1 = TestDataFactory.productionBatchEntity(TestDataFactory.RECIPE_ID, BatchStatus.PLANNED);
        ProductionBatch b2 = TestDataFactory.productionBatchEntity(UUID.randomUUID(), BatchStatus.COMPLETED);

        ProductionBatchDto d1 = TestDataFactory.productionBatchDto();
        ProductionBatchDto d2 = TestDataFactory.productionBatchDto();

        Mockito.when(productionBatchService.findAll())
            .thenReturn(List.of(b1, b2));

        Mockito.when(productionBatchMapper.mapToDto(Mockito.any()))
            .thenReturn(d1, d2);

        List<ProductionBatchDto> result =
            productionBatchFacade.findAllBatches();

        assertThat(result).containsExactly(d1, d2);
    }

    @Test
    void findBatch_existingId_returnsDto() {
        UUID id = TestDataFactory.BATCH_ID;

        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        ProductionBatchDto dto = TestDataFactory.productionBatchDto();

        Mockito.when(productionBatchService.findById(id)).thenReturn(batch);
        Mockito.when(productionBatchMapper.mapToDto(batch)).thenReturn(dto);

        ProductionBatchDto result = productionBatchFacade.findBatch(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void updateBatchStatus_validStatus_returnsDto() {
        UUID id = TestDataFactory.BATCH_ID;

        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        ProductionBatchDto dto = TestDataFactory.productionBatchDto();

        Mockito.when(productionBatchService.updateStatus(id, BatchStatus.PLANNED))
            .thenReturn(batch);

        Mockito.when(productionBatchMapper.mapToDto(batch))
            .thenReturn(dto);

        ProductionBatchDto result =
            productionBatchFacade.updateBatchStatus(id, BatchStatus.PLANNED);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void deleteBatch_existingId_callsService() {
        UUID id = TestDataFactory.BATCH_ID;

        productionBatchFacade.deleteBatch(id);

        Mockito.verify(productionBatchService).deleteById(id);
    }
}
