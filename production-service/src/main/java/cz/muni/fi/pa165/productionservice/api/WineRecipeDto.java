package cz.muni.fi.pa165.productionservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class WineRecipeDto {
    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @Positive
    private int productionYear;

    @Min(0)
    @Max(100)
    private double alcoholPercentage;
    private String tastingNotes;

    @Positive
    private double bottleVolumeLiters;

    @Valid
    private List<GrapeComponentDto> grapeComponents = new ArrayList<>();

    @Valid
    private List<AdditionalIngredientDto> additionalIngredients = new ArrayList<>();
}
