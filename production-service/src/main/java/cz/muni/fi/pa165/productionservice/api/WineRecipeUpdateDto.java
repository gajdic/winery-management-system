package cz.muni.fi.pa165.productionservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Request body for updating a wine recipe.")
public class WineRecipeUpdateDto {
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

    @NotBlank
    @Schema(description = "Recipe tasting notes.", example = "Fresh citrus aroma with mineral finish.")
    private String tastingNotes;

    @Positive
    @Schema(description = "Bottle volume in liters.", example = "0.75", minimum = "0", exclusiveMinimum = true)
    private double bottleVolumeLiters;

    @Valid
    @NotEmpty
    @Schema(description = "Required grape composition for the recipe.")
    private List<GrapeComponentDto> grapeComponents = new ArrayList<>();

    @Valid
    @Schema(description = "Optional additional ingredients for the recipe.")
    private List<AdditionalIngredientDto> additionalIngredients = new ArrayList<>();

    public void setGrapeComponents(List<GrapeComponentDto> grapeComponents) {
        this.grapeComponents = grapeComponents != null ? grapeComponents : new ArrayList<>();
    }

    public void setAdditionalIngredients(List<AdditionalIngredientDto> additionalIngredients) {
        this.additionalIngredients = additionalIngredients != null ? additionalIngredients : new ArrayList<>();
    }
}
