package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.enums.QualityRecommendation;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductionPlanningServiceTest {

    private static final UUID MERLOT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CABERNET_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID VINEYARD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private final ProductionPlanningService service = new ProductionPlanningService();

    @Test
    void createProductionPlan_validInput_returnsCorrectPlan() {
        // Arrange
        WineRecipe recipe = new WineRecipe();
        recipe.setId(UUID.randomUUID());
        recipe.setBottleVolumeLiters(0.75);

        GrapeComponent comp = new GrapeComponent(MERLOT_ID.toString(), 2, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot(MERLOT_ID,
            VINEYARD_ID,
            LocalDate.now(),
            200,
            85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            100,
            List.of(lot)
        );

        assertThat(result.getRecipeId()).isEqualTo(recipe.getId());
        assertThat(result.getRequestedBottleCount()).isEqualTo(100);
        assertThat(result.getFeasibleBottleCount()).isGreaterThan(0);
        assertThat(result.getProducibleLiters()).isGreaterThan(0);
        assertThat(result.getLimitingGrapeType()).isEqualTo(MERLOT_ID.toString());
    }

    @Test
    void createProductionPlan_multipleComponents_detectsLimitingGrape() {
        WineRecipe recipe = new WineRecipe();
        recipe.setId(UUID.randomUUID());
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent merlot = new GrapeComponent(MERLOT_ID.toString(), 1, 80);

        GrapeComponent cabernet = new GrapeComponent(CABERNET_ID.toString(), 1, 80);

        recipe.setGrapeComponents(List.of(merlot, cabernet));

        HarvestLot lot1 = new HarvestLot(MERLOT_ID,
            VINEYARD_ID,
            LocalDate.now(),
            100,
            85);

        HarvestLot lot2 = new HarvestLot(CABERNET_ID,
            VINEYARD_ID,
            LocalDate.now(),
            50,
            85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            200,
            List.of(lot1, lot2)
        );

        assertThat(result.getLimitingGrapeType()).isEqualTo(CABERNET_ID.toString());
    }

    @Test
    void createProductionPlan_enoughResources_setsFullyFeasibleTrue() {
        WineRecipe recipe = new WineRecipe();
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent comp = new GrapeComponent(MERLOT_ID.toString(), 1, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot(MERLOT_ID,
            VINEYARD_ID,
            LocalDate.now(),
            1000,
            85);

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
        WineRecipe recipe = new WineRecipe();
        recipe.setBottleVolumeLiters(1.0);
        recipe.setGrapeComponents(List.of());

        assertThrows(IllegalArgumentException.class, () ->
            service.createProductionPlan(recipe, 10, List.of())
        );
    }

    @Test
    void evaluateBlendQuality_highQuality_returnsPremiumRecommendation() {
        WineRecipe recipe = new WineRecipe();
        recipe.setId(UUID.randomUUID());

        GrapeComponent comp = new GrapeComponent(MERLOT_ID.toString(), 1, 70);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot(MERLOT_ID,
            VINEYARD_ID,
            LocalDate.now(),
            200,
            90);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getWeightedQualityScore()).isGreaterThanOrEqualTo(90);
        assertThat(result.getRecommendation()).isEqualTo(QualityRecommendation.PREMIUM_BATCH_CANDIDATE);
    }

    @Test
    void evaluateBlendQuality_lowQuality_returnsWarningRecommendation() {
        WineRecipe recipe = new WineRecipe();

        GrapeComponent comp = new GrapeComponent(MERLOT_ID.toString(), 1, 90);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot(MERLOT_ID,
            VINEYARD_ID,
            LocalDate.now(),
            200,
            50);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getRecommendation()).isEqualTo(QualityRecommendation.BELOW_TARGET_QUALITY);
    }
}
