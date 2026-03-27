package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductionPlanningServiceTest {

    private final ProductionPlanningService service = new ProductionPlanningService();

    @Test
    void createProductionPlan_validInput_returnsCorrectPlan() {
        // Arrange
        WineRecipe recipe = new WineRecipe();
        recipe.setId(UUID.randomUUID());
        recipe.setBottleVolumeLiters(0.75);

        GrapeComponent comp = new GrapeComponent("Merlot", 2, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot("Merlot",
            "Some Vineyard",
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
        assertThat(result.getLimitingGrapeType()).isEqualTo("Merlot");
    }

    @Test
    void createProductionPlan_multipleComponents_detectsLimitingGrape() {
        WineRecipe recipe = new WineRecipe();
        recipe.setId(UUID.randomUUID());
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent merlot = new GrapeComponent("Merlot", 1, 80);

        GrapeComponent cabernet = new GrapeComponent("Cabernet", 1, 80);

        recipe.setGrapeComponents(List.of(merlot, cabernet));

        HarvestLot lot1 = new HarvestLot("Merlot",
            "Some Vineyard",
            LocalDate.now(),
            100,
            85);

        HarvestLot lot2 = new HarvestLot("Cabernet",
            "Some Vineyard",
            LocalDate.now(),
            50,
            85);

        ProductionPlanDto result = service.createProductionPlan(
            recipe,
            200,
            List.of(lot1, lot2)
        );

        assertThat(result.getLimitingGrapeType()).isEqualTo("Cabernet");
    }

    @Test
    void createProductionPlan_enoughResources_setsFullyFeasibleTrue() {
        WineRecipe recipe = new WineRecipe();
        recipe.setBottleVolumeLiters(1.0);

        GrapeComponent comp = new GrapeComponent("Merlot", 1, 80);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot("Merlot",
            "Some Vineyard",
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

        GrapeComponent comp = new GrapeComponent("Merlot", 1, 70);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot("Merlot",
            "Some Vineyard",
            LocalDate.now(),
            200,
            90);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getWeightedQualityScore()).isGreaterThanOrEqualTo(90);
        assertThat(result.getRecommendation()).contains("Premium");
    }

    @Test
    void evaluateBlendQuality_lowQuality_returnsWarningRecommendation() {
        WineRecipe recipe = new WineRecipe();

        GrapeComponent comp = new GrapeComponent("Merlot",1, 90);

        recipe.setGrapeComponents(List.of(comp));

        HarvestLot lot = new HarvestLot("Merlot",
            "Some Vineyard",
            LocalDate.now(),
            200,
            50);

        QualityEvaluationDto result = service.evaluateBlendQuality(recipe, List.of(lot));

        assertThat(result.getRecommendation()).contains("below target");
    }
}
