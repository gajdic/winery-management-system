package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductionBatchCreateDto {
    @NotNull
    private UUID recipeId;

    @Positive
    private int desiredBottleCount;
}
