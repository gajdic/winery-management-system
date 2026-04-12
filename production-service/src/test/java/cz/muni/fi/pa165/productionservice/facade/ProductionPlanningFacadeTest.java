package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.HarvestLotMapper;
import cz.muni.fi.pa165.productionservice.service.ProductionPlanningService;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductionPlanningFacadeTest {

    @Mock
    private ProductionPlanningService productionPlanningService;

    @Mock
    private HarvestLotMapper harvestLotMapper;

    @Mock
    private WineRecipeService wineRecipeService;

    @InjectMocks
    private ProductionPlanningFacade productionPlanningFacade;

    @Test
    void planProduction_validRequest_returnsProductionPlanDto() {
        ProductionPlanRequestDto request = TestDataFactory.productionPlanRequestDto();

        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        ProductionPlanDto expectedDto = TestDataFactory.productionPlanDto();

        List<HarvestLot> harvestEntities = List.of(TestDataFactory.harvestLot());

        Mockito.when(wineRecipeService.findById(TestDataFactory.RECIPE_ID)).thenReturn(recipe);
        Mockito.when(harvestLotMapper.toHarvestEntities(request.getAvailableHarvests()))
            .thenReturn(harvestEntities);

        Mockito.when(productionPlanningService.createProductionPlan(
            recipe,
            100,
            harvestEntities
        )).thenReturn(expectedDto);

        ProductionPlanDto result = productionPlanningFacade.planProduction(request);

        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void evaluateQuality_validRequest_returnsQualityEvaluationDto() {
        QualityEvaluationRequestDto request = TestDataFactory.qualityEvaluationRequestDto();

        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        QualityEvaluationDto expectedDto = TestDataFactory.qualityEvaluationDto();

        List<HarvestLot> harvestEntities = List.of(TestDataFactory.harvestLot());

        Mockito.when(wineRecipeService.findById(TestDataFactory.RECIPE_ID)).thenReturn(recipe);
        Mockito.when(harvestLotMapper.toHarvestEntities(request.getHarvestLots()))
            .thenReturn(harvestEntities);

        Mockito.when(productionPlanningService.evaluateBlendQuality(recipe, harvestEntities))
            .thenReturn(expectedDto);

        QualityEvaluationDto result = productionPlanningFacade.evaluateQuality(request);

        assertThat(result).isEqualTo(expectedDto);
    }
}
