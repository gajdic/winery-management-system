package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Body for creating a new review")
public class ReviewCreateDto {

    @NotNull
    @Schema(description = "Id of the wine") //TODO
    private UUID wineId;

    @NotNull
    @Schema(description = "Id of the customer") //TODO
    private UUID customerID;

    @Min(0)
    @Max(5)
    @Schema(description = "Numerical rating of the wine between 1 and 5", example = "4")
    private int rating;

    @Schema(description = "Optional text description of the bottle", example = "The wine is a bit more dry then I expected")
    private String comment;

    public UUID getWineId() {
        return wineId;
    }

    public void setWineId(UUID wineId) {
        this.wineId = wineId;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
