package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.enums.QualityRecommendation;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductionPlanningServiceTest {

    private final ProductionPlanningService service = new ProductionPlanningService();

    @Test
    void createProductionPlan_validInput_returnsCorrectPlan() {
        // Arrange
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        recipe.setBottleVolumeLiters(0.75);

        GrapeComponent comp = TestDataFactory.grapeComponent(TestDataFactory.MERLOT_ID, 2, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = TestDataFactory.harvestLot(TestDataFactory.MERLOT_ID, 200, 85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            100,
            List.of(lot)
        );

        assertThat(result.getRecipeId()).isEqualTo(recipe.getId());
        assertThat(result.getRequestedBottleCount()).isEqualTo(100);
        assertThat(result.getFeasibleBottleCount()).isGreaterThan(0);
        assertThat(result.getProducibleLiters()).isGreaterThan(0);
        assertThat(result.getLimitingGrapeType()).isEqualTo(TestDataFactory.MERLOT_ID.toString());
    }

    @Test
    void createProductionPlan_multipleComponents_detectsLimitingGrape() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent merlot = TestDataFactory.grapeComponent(TestDataFactory.MERLOT_ID, 1, 80);

        GrapeComponent cabernet = TestDataFactory.grapeComponent(TestDataFactory.CABERNET_ID, 1, 80);

        recipe.setGrapeComponents(List.of(merlot, cabernet));

        HarvestLot lot1 = TestDataFactory.harvestLot(TestDataFactory.MERLOT_ID, 100, 85);

        HarvestLot lot2 = TestDataFactory.harvestLot(TestDataFactory.CABERNET_ID, 50, 85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            200,
            List.of(lot1, lot2)
        );

        assertThat(result.getLimitingGrapeType()).isEqualTo(TestDataFactory.CABERNET_ID.toString());
    }

    @Test
    void createProductionPlan_enoughResources_setsFullyFeasibleTrue() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent comp = TestDataFactory.grapeComponent(TestDataFactory.MERLOT_ID, 1, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = TestDataFactory.harvestLot(TestDataFactory.MERLOT_ID, 1000, 85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            10,
            List.of(lot)
        );

        assertThat(result.isFullyFeasible()).isTrue();
    }

    @Test
    void createProductionPlan_nullRecipe_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            service.createProductionPlan(null, 10, List.of())
        );
    }

    @Test
    void createProductionPlan_emptyComponents_throwsException() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();
        recipe.setBottleVolumeLiters(1.0);
        recipe.setGrapeComponents(List.of());

        assertThrows(IllegalArgumentException.class, () ->
            service.createProductionPlan(recipe, 10, List.of())
        );
    }

    @Test
    void evaluateBlendQuality_highQuality_returnsPremiumRecommendation() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();

        GrapeComponent comp = TestDataFactory.grapeComponent(TestDataFactory.MERLOT_ID, 1, 70);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = TestDataFactory.harvestLot(TestDataFactory.MERLOT_ID, 200, 90);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getWeightedQualityScore()).isGreaterThanOrEqualTo(90);
        assertThat(result.getRecommendation()).isEqualTo(QualityRecommendation.PREMIUM_BATCH_CANDIDATE);
    }

    @Test
    void evaluateBlendQuality_lowQuality_returnsWarningRecommendation() {
        WineRecipe recipe = TestDataFactory.wineRecipeEntity();

        GrapeComponent comp = TestDataFactory.grapeComponent(TestDataFactory.MERLOT_ID, 1, 90);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = TestDataFactory.harvestLot(TestDataFactory.MERLOT_ID, 200, 50);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getRecommendation()).isEqualTo(QualityRecommendation.BELOW_TARGET_QUALITY);
    }
}
