package cz.muni.fi.pa165.customerservice.data.model;

import java.util.Objects;
import java.util.UUID;

public class Review {
    private UUID id;
    private UUID customerId;
    private UUID wineId;
    private int rating;
    private String comment;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getWineId() {
        return wineId;
    }

    public void setWineId(UUID wineId) {
        this.wineId = wineId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && Objects.equals(id, review.id) && Objects.equals(customerId, review.customerId) && Objects.equals(wineId, review.wineId) && Objects.equals(comment, review.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, wineId, rating, comment);
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + id +
            ", customerId=" + customerId +
            ", wineId=" + wineId +
            ", rating=" + rating +
            ", comment='" + comment + '\'' +
            '}';
    }
}
