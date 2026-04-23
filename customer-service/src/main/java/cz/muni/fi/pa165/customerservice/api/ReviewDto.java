package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Information about an existing review")
public class ReviewDto {

    @NotNull
    @Schema(description = "Unique identifier of the review", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID id;

    @NotNull
    @Schema(description = "Id of the wine the review is about", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID wineId;

    @NotNull
    @Schema(description = "Id of the customer") //TODO
    private UUID customerId;

    @Min(0)
    @Max(5)
    @Schema(description = "Numerical rating of the wine between 1 and 5", example = "4")
    private int rating;

    @Schema(description = "Optional text description of the bottle", example = "The wine is a bit more dry then I expected")
    private String comment;
}
