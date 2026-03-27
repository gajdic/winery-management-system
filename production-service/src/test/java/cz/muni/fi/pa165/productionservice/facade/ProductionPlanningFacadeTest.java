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
    void planProduction_returnsProductionPlanDto() {
        UUID recipeId = UUID.randomUUID();

        ProductionPlanRequestDto request = new ProductionPlanRequestDto();
        request.setRecipeId(recipeId);
        request.setDesiredBottleCount(100);

        WineRecipe recipe = new WineRecipe();
        ProductionPlanDto expectedDto = new ProductionPlanDto();

        List<HarvestLot> harvestEntities = List.of(new HarvestLot(
            "Merlot",
            "Vineyard A",
            LocalDate.now(),
            100.0,
            85
        ));

        Mockito.when(wineRecipeService.findById(recipeId)).thenReturn(recipe);
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
    void evaluateQuality_returnsQualityEvaluationDto() {
        UUID recipeId = UUID.randomUUID();

        QualityEvaluationRequestDto request = new QualityEvaluationRequestDto();
        request.setRecipeId(recipeId);

        WineRecipe recipe = new WineRecipe();
        QualityEvaluationDto expectedDto = new QualityEvaluationDto();

        List<HarvestLot> harvestEntities = List.of(
            new HarvestLot("Merlot", "Vineyard A", LocalDate.now(), 100.0, 85)
        );

        Mockito.when(wineRecipeService.findById(recipeId)).thenReturn(recipe);
        Mockito.when(harvestLotMapper.toHarvestEntities(request.getHarvestLots()))
            .thenReturn(harvestEntities);

        Mockito.when(productionPlanningService.evaluateBlendQuality(recipe, harvestEntities))
            .thenReturn(expectedDto);

        QualityEvaluationDto result = productionPlanningFacade.evaluateQuality(request);

        assertThat(result).isEqualTo(expectedDto);
    }
}
