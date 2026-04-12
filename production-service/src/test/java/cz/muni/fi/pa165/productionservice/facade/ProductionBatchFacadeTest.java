package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.HarvestLotDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.mappers.ProductionBatchMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionBatchService;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductionBatchFacadeTest {

    private static final UUID MERLOT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID VINEYARD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Mock
    private ProductionBatchService productionBatchService;

    @Mock
    private ProductionBatchMapper productionBatchMapper;

    @Mock
    private WineRecipeService wineRecipeService;

    @Mock
    private ProductionPlanningService productionPlanningService;

    @Mock
    private HarvestLotMapper harvestLotMapper;

    @InjectMocks
    private ProductionBatchFacade productionBatchFacade;

    @Test
    void createBatch_validRequest_returnsDto() {
        UUID recipeId = UUID.randomUUID();

        ProductionBatchCreateDto dto = new ProductionBatchCreateDto();
        dto.setRecipeId(recipeId);
        dto.setDesiredBottleCount(100);

        WineRecipe recipe = new WineRecipe();
        ProductionPlanDto plan = new ProductionPlanDto();

        List<HarvestLotDto> harvestDtos = List.of(new HarvestLotDto());
        List<HarvestLot> harvestEntities = List.of(
            new HarvestLot(MERLOT_ID,
                VINEYARD_ID,
                LocalDate.now(),
                100.0,
                85)
        );

        ProductionBatch batch = new ProductionBatch();
        ProductionBatchDto batchDto = new ProductionBatchDto();

        Mockito.when(wineRecipeService.findById(recipeId)).thenReturn(recipe);

        Mockito.when(harvestLotMapper.toHarvestEntities(harvestDtos))
            .thenReturn(harvestEntities);

        Mockito.when(productionPlanningService.createProductionPlan(
            recipe,
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
        ProductionBatch b1 = new ProductionBatch();
        ProductionBatch b2 = new ProductionBatch();

        ProductionBatchDto d1 = new ProductionBatchDto();
        ProductionBatchDto d2 = new ProductionBatchDto();

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
        UUID id = UUID.randomUUID();

        ProductionBatch batch = new ProductionBatch();
        ProductionBatchDto dto = new ProductionBatchDto();

        Mockito.when(productionBatchService.findById(id)).thenReturn(batch);
        Mockito.when(productionBatchMapper.mapToDto(batch)).thenReturn(dto);

        ProductionBatchDto result = productionBatchFacade.findBatch(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void updateBatchStatus_validStatus_returnsDto() {
        UUID id = UUID.randomUUID();

        ProductionBatch batch = new ProductionBatch();
        ProductionBatchDto dto = new ProductionBatchDto();

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
        UUID id = UUID.randomUUID();

        productionBatchFacade.deleteBatch(id);

        Mockito.verify(productionBatchService).deleteById(id);
    }
}
