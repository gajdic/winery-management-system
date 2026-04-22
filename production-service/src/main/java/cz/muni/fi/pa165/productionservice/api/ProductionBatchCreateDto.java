package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Production batch creation data.")
public class ProductionBatchCreateDto {
    @NotNull
    @Schema(description = "Identifier of the recipe used for the batch.", example = "33333333-3333-3333-3333-333333333333")
    private UUID recipeId;

    @Positive
    @Schema(description = "Requested number of bottles to produce.", example = "1000", minimum = "1")
    private int desiredBottleCount;
}
