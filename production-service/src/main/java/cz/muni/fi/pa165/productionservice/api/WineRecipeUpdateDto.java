package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WineRecipeUpdateDto {
    @NotBlank
    private String name;

    @Positive
    private int productionYear;

    @Min(0)
    @Max(100)
    private double alcoholPercentage;

    @NotBlank
    private String tastingNotes;

    @Positive
    private double bottleVolumeLiters;

    @Valid
    @NotEmpty
    private List<GrapeComponentDto> grapeComponents = new ArrayList<>();

    @Valid
    private List<AdditionalIngredientDto> additionalIngredients = new ArrayList<>();

    public void setGrapeComponents(List<GrapeComponentDto> grapeComponents) {
        this.grapeComponents = grapeComponents != null ? grapeComponents : new ArrayList<>();
    }

    public void setAdditionalIngredients(List<AdditionalIngredientDto> additionalIngredients) {
        this.additionalIngredients = additionalIngredients != null ? additionalIngredients : new ArrayList<>();
    }
}
