package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Wine recipe returned by the production service.")
public class WineRecipeDto {
    @NotNull
    @Schema(description = "Wine recipe identifier.", example = "33333333-3333-3333-3333-333333333333")
    private UUID id;

    @NotBlank
    @Schema(description = "Wine recipe name.", example = "Riesling")
    private String name;

    @Positive
    @Schema(description = "Wine production year.", example = "2025", minimum = "1")
    private int productionYear;

    @Min(0)
    @Max(100)
    @Schema(description = "Expected alcohol percentage.", example = "12.5", minimum = "0", maximum = "100")
    private double alcoholPercentage;
    @Schema(description = "Recipe tasting notes.", example = "Fresh citrus aroma with mineral finish.")
    private String tastingNotes;

    @Positive
    @Schema(description = "Bottle volume in liters.", example = "0.75", minimum = "0", exclusiveMinimum = true)
    private double bottleVolumeLiters;

    @Valid
    @Schema(description = "Required grape composition for the recipe.")
    private List<GrapeComponentDto> grapeComponents = new ArrayList<>();

    @Valid
    @Schema(description = "Additional ingredients for the recipe.")
    private List<AdditionalIngredientDto> additionalIngredients = new ArrayList<>();
}
