package cz.muni.fi.pa165.productionservice.api;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductionPlanDto {
    private UUID recipeId;
    private int requestedBottleCount;
    private int feasibleBottleCount;
    private double producibleLiters;
    private boolean fullyFeasible;
    private String limitingGrapeType;
    private int estimatedBlendQuality;
}
