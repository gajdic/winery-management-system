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
@Schema(description = "Body for creating a new review")
public class ReviewCreateDto {

    @NotNull
    @Schema(description = "Id of the wine") //TODO
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
