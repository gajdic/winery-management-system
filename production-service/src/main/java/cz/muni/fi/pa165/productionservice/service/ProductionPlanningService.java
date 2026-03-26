package cz.muni.fi.pa165.productionservice.service;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.data.model.GrapeComponent;
import cz.muni.fi.pa165.productionservice.data.model.HarvestLot;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductionPlanningService {
    public ProductionPlanDto createProductionPlan(WineRecipe recipe, int desiredBottleCount, List<HarvestLot> harvestLots) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null.");
        }
        if (harvestLots == null) {
            throw new IllegalArgumentException("Harvest lots must not be null.");
        }
        if (recipe.getGrapeComponents() == null || recipe.getGrapeComponents().isEmpty()) {
            throw new IllegalArgumentException("Recipe must contain at least one grape component.");
        }
        if (recipe.getBottleVolumeLiters() <= 0) {
            throw new IllegalArgumentException("Bottle volume must be greater than zero.");
        }

        double requestedLiters = desiredBottleCount * recipe.getBottleVolumeLiters();

        double maxLiters = recipe.getGrapeComponents().stream()
            .mapToDouble(component -> calculatePossibleLitersForComponent(component, harvestLots))
            .min()
            .orElse(0.0);

        GrapeComponent limitingComponent = recipe.getGrapeComponents().stream()
            .min(Comparator.comparingDouble(component -> calculatePossibleLitersForComponent(component, harvestLots)))
            .orElse(null);

        int feasibleBottleCount = (int) Math.floor(maxLiters / recipe.getBottleVolumeLiters());
        QualityEvaluationDto qualityEvaluation = evaluateBlendQuality(recipe, harvestLots);

        ProductionPlanDto planDto = new ProductionPlanDto();
        planDto.setRecipeId(recipe.getId());
        planDto.setRequestedBottleCount(desiredBottleCount);
        planDto.setFeasibleBottleCount(Math.min(feasibleBottleCount, desiredBottleCount));
        planDto.setProducibleLiters(Math.min(maxLiters, requestedLiters));
        planDto.setFullyFeasible(feasibleBottleCount >= desiredBottleCount);
        planDto.setLimitingGrapeType(limitingComponent == null ? null : limitingComponent.getGrapeType());
        planDto.setEstimatedBlendQuality(qualityEvaluation.getWeightedQualityScore());
        return planDto;
    }

    private double calculatePossibleLitersForComponent(GrapeComponent component, List<HarvestLot> harvestLots) {
        double availableKilograms = harvestLots.stream()
            .filter(h -> h.getGrapeType().equalsIgnoreCase(component.getGrapeType()))
            .mapToDouble(HarvestLot::getAvailableKilograms)
            .sum();
        return availableKilograms / component.getKilogramsPerLiter();
    }

    public QualityEvaluationDto evaluateBlendQuality(WineRecipe recipe, List<HarvestLot> harvestLots) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null.");
        }
        if (harvestLots == null) {
            throw new IllegalArgumentException("Harvest lots must not be null.");
        }
        if (recipe.getGrapeComponents() == null || recipe.getGrapeComponents().isEmpty()) {
            throw new IllegalArgumentException("Recipe must contain at least one grape component.");
        }

        int weightedQuality = calculateWeightedQuality(recipe, harvestLots);
        int targetQuality = (int) Math.round(recipe.getGrapeComponents().stream()
            .mapToInt(GrapeComponent::getPreferredQuality)
            .average()
            .orElse(0));

        return getQualityEvaluationDto(recipe, weightedQuality, targetQuality);
    }

    private static @NonNull QualityEvaluationDto getQualityEvaluationDto(WineRecipe recipe, int weightedQuality, int targetQuality) {
        QualityEvaluationDto evaluationDto = new QualityEvaluationDto();
        evaluationDto.setRecipeId(recipe.getId());
        evaluationDto.setWeightedQualityScore(weightedQuality);
        evaluationDto.setRecipeQualityTargetMet(weightedQuality >= targetQuality);
        if (weightedQuality >= targetQuality + 10) {
            evaluationDto.setRecommendation("Premium batch candidate.");
        } else if (weightedQuality >= targetQuality) {
            evaluationDto.setRecommendation("Suitable for planned production.");
        } else {
            evaluationDto.setRecommendation("Blend quality is below target, consider using higher-quality harvest lots.");
        }
        return evaluationDto;
    }

    private int calculateWeightedQuality(WineRecipe recipe, List<HarvestLot> harvestLots) {
        double totalRequiredWeight = recipe.getGrapeComponents().stream()
            .mapToDouble(GrapeComponent::getKilogramsPerLiter)
            .sum();

        double weightedQuality = recipe.getGrapeComponents().stream()
            .mapToDouble(component -> {
                double componentQuality = harvestLots.stream()
                    .filter(h -> h.getGrapeType().equalsIgnoreCase(component.getGrapeType()))
                    .mapToInt(HarvestLot::getQualityScore)
                    .average()
                    .orElse(0);
                return componentQuality * component.getKilogramsPerLiter();
            })
            .sum();

        if (totalRequiredWeight == 0) {
            return 0;
        }
        return (int) Math.round(weightedQuality / totalRequiredWeight);
    }
}
