package cz.muni.fi.pa165.customerservice.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public class ReviewUpdateDto {
    private UUID wineId;

    private UUID customerId;

    @Min(0)
    @Max(5)
    private int rating;

    private String comment;

    public UUID getWineId() {
        return wineId;
    }

    public void setWineId(UUID wineId) {
        this.wineId = wineId;
    }

    public UUID getCustomerID() {
        return customerId;
    }

    public void setCustomerID(UUID customerID) {
        this.customerId = customerID;
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
