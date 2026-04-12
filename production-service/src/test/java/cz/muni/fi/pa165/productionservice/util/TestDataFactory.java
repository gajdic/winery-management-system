package cz.muni.fi.pa165.productionservice.util;

import cz.muni.fi.pa165.productionservice.api.*;
import cz.muni.fi.pa165.productionservice.api.enums.QualityRecommendation;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.data.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class TestDataFactory {

    public static final UUID RECIPE_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID BATCH_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID MERLOT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID CABERNET_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    public static final UUID VINEYARD_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
    public static final LocalDate HARVEST_DATE = LocalDate.of(2026, 9, 15);
    public static final LocalDateTime BATCH_CREATED_AT = LocalDateTime.of(2026, 4, 11, 12, 0);

    private TestDataFactory() {
    }

    public static WineRecipe wineRecipeEntity() {
        return wineRecipeEntity("Riesling", 2026);
    }

    public static WineRecipe wineRecipeEntity(String name, int productionYear) {
        WineRecipe recipe = new WineRecipe();
        recipe.setId(RECIPE_ID);
        recipe.setName(name);
        recipe.setProductionYear(productionYear);
        recipe.setAlcoholPercentage(12.5);
        recipe.setTastingNotes("Fresh citrus notes");
        recipe.setBottleVolumeLiters(0.75);
        recipe.setGrapeComponents(List.of(grapeComponent()));
        recipe.setAdditionalIngredients(List.of(additionalIngredient()));
        return recipe;
    }

    public static GrapeComponent grapeComponent() {
        return new GrapeComponent("Riesling", 1.3, 85);
    }

    public static GrapeComponent grapeComponent(UUID grapeTypeId, double kilogramsPerLiter, int preferredQuality) {
        return new GrapeComponent(grapeTypeId.toString(), kilogramsPerLiter, preferredQuality);
    }

    public static AdditionalIngredient additionalIngredient() {
        return new AdditionalIngredient("Yeast", 0.02, "kg");
    }

    public static HarvestLot harvestLot() {
        return harvestLot(MERLOT_ID, 200.0, 85);
    }

    public static HarvestLot harvestLot(UUID grapeTypeId, double availableKilograms, int qualityScore) {
        return new HarvestLot(grapeTypeId, VINEYARD_ID, HARVEST_DATE, availableKilograms, qualityScore);
    }

    public static ProductionBatch productionBatchEntity() {
        return productionBatchEntity(RECIPE_ID, BatchStatus.PLANNED);
    }

    public static ProductionBatch productionBatchEntity(UUID recipeId, BatchStatus status) {
        ProductionBatch batch = new ProductionBatch();
        batch.setId(BATCH_ID);
        batch.setRecipeId(recipeId);
        batch.setPlannedLiters(75.0);
        batch.setPlannedBottleCount(100);
        batch.setActualBottleCount(0);
        batch.setStatus(status);
        batch.setCreatedAt(BATCH_CREATED_AT);
        return batch;
    }

    public static WineRecipeDto wineRecipeDto() {
        WineRecipeDto dto = new WineRecipeDto();
        dto.setId(RECIPE_ID);
        dto.setName("Riesling");
        dto.setProductionYear(2026);
        dto.setAlcoholPercentage(12.5);
        dto.setTastingNotes("Fresh citrus notes");
        dto.setBottleVolumeLiters(0.75);
        dto.setGrapeComponents(List.of(grapeComponentDto()));
        dto.setAdditionalIngredients(List.of(additionalIngredientDto()));
        return dto;
    }

    public static WineRecipeCreateDto wineRecipeCreateDto() {
        WineRecipeCreateDto dto = new WineRecipeCreateDto();
        dto.setName("Riesling");
        dto.setProductionYear(2026);
        dto.setAlcoholPercentage(12.5);
        dto.setTastingNotes("Fresh citrus notes");
        dto.setBottleVolumeLiters(0.75);
        dto.setGrapeComponents(List.of(grapeComponentDto()));
        dto.setAdditionalIngredients(List.of(additionalIngredientDto()));
        return dto;
    }

    public static WineRecipeUpdateDto wineRecipeUpdateDto() {
        WineRecipeUpdateDto dto = new WineRecipeUpdateDto();
        dto.setName("Updated Riesling");
        dto.setProductionYear(2025);
        dto.setAlcoholPercentage(12.5);
        dto.setTastingNotes("Fresh citrus notes");
        dto.setBottleVolumeLiters(0.75);
        dto.setGrapeComponents(List.of(grapeComponentDto()));
        dto.setAdditionalIngredients(List.of(additionalIngredientDto()));
        return dto;
    }

    public static GrapeComponentDto grapeComponentDto() {
        GrapeComponentDto dto = new GrapeComponentDto();
        dto.setGrapeType("Riesling");
        dto.setKilogramsPerLiter(1.3);
        dto.setPreferredQuality(85);
        return dto;
    }

    public static AdditionalIngredientDto additionalIngredientDto() {
        AdditionalIngredientDto dto = new AdditionalIngredientDto();
        dto.setName("Yeast");
        dto.setQuantityPerLiter(0.02);
        dto.setUnit("kg");
        return dto;
    }

    public static HarvestLotDto harvestLotDto() {
        HarvestLotDto dto = new HarvestLotDto();
        dto.setGrapeTypeId(MERLOT_ID);
        dto.setVineyardId(VINEYARD_ID);
        dto.setHarvestDate(HARVEST_DATE);
        dto.setAvailableKilograms(200.0);
        dto.setQualityScore(85);
        return dto;
    }

    public static ProductionBatchDto productionBatchDto() {
        ProductionBatchDto dto = new ProductionBatchDto();
        dto.setId(BATCH_ID);
        dto.setRecipeId(RECIPE_ID);
        dto.setPlannedLiters(75.0);
        dto.setPlannedBottleCount(100);
        dto.setActualBottleCount(0);
        dto.setStatus(BatchStatus.PLANNED);
        dto.setCreatedAt(BATCH_CREATED_AT);
        return dto;
    }

    public static ProductionBatchCreateDto productionBatchCreateDto() {
        ProductionBatchCreateDto dto = new ProductionBatchCreateDto();
        dto.setRecipeId(RECIPE_ID);
        dto.setDesiredBottleCount(100);
        return dto;
    }

    public static ProductionBatchCreateRequestDto productionBatchCreateRequestDto() {
        ProductionBatchCreateRequestDto dto = new ProductionBatchCreateRequestDto();
        dto.setBatch(productionBatchCreateDto());
        dto.setHarvestLots(List.of(harvestLotDto()));
        return dto;
    }

    public static ProductionPlanDto productionPlanDto() {
        ProductionPlanDto dto = new ProductionPlanDto();
        dto.setRecipeId(RECIPE_ID);
        dto.setRequestedBottleCount(100);
        dto.setFeasibleBottleCount(100);
        dto.setProducibleLiters(75.0);
        dto.setFullyFeasible(true);
        dto.setLimitingGrapeType(MERLOT_ID.toString());
        dto.setEstimatedBlendQuality(85);
        return dto;
    }

    public static ProductionPlanRequestDto productionPlanRequestDto() {
        ProductionPlanRequestDto dto = new ProductionPlanRequestDto();
        dto.setRecipeId(RECIPE_ID);
        dto.setDesiredBottleCount(100);
        dto.setAvailableHarvests(List.of(harvestLotDto()));
        return dto;
    }

    public static QualityEvaluationDto qualityEvaluationDto() {
        QualityEvaluationDto dto = new QualityEvaluationDto();
        dto.setRecipeId(RECIPE_ID);
        dto.setWeightedQualityScore(85);
        dto.setRecipeQualityTargetMet(true);
        dto.setRecommendation(QualityRecommendation.SUITABLE_FOR_PRODUCTION);
        return dto;
    }

    public static QualityEvaluationRequestDto qualityEvaluationRequestDto() {
        QualityEvaluationRequestDto dto = new QualityEvaluationRequestDto();
        dto.setRecipeId(RECIPE_ID);
        dto.setHarvestLots(List.of(harvestLotDto()));
        return dto;
    }

    public static List<WineRecipeDto> wineRecipeDtos() {
        return List.of(wineRecipeDto(), wineRecipeDto());
    }

    public static List<ProductionBatchDto> productionBatchDtos() {
        return List.of(productionBatchDto(), productionBatchDto());
    }
}
