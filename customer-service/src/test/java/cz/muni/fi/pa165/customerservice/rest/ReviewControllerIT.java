package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void getReview_existingReview_returnsReviewDto() throws Exception {
        // Arrange
        Review review = createReview("Excellent wine");
        // Act
        review = reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(get("/reviews/{id}", review.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(review.getId().toString()))
            .andExpect(jsonPath("$.wineId").value(review.getWineId().toString()))
            .andExpect(jsonPath("$.customerID").value(review.getCustomerId().toString()))
            .andExpect(jsonPath("$.rating").value(5))
            .andExpect(jsonPath("$.comment").value("Excellent wine"));
    }

    @Test
    void getReview_nonExistingReview_returnsNotFound() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();

        // Act + Assert
        mockMvc.perform(get("/reviews/{id}", reviewId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Review not found: " + reviewId));
    }

    @Test
    void getAllReviews_returnsReviewsIncludingInsertedReview() throws Exception {
        // Arrange
        Review review = createReview("Collection review");
        // Act
        reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(get("/reviews")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].comment", hasItem("Collection review")));
    }

    @Test
    void getReviewsByCustomer_existingCustomer_returnsMatchingReviews() throws Exception {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Review review = createReview("Customer filter review");
        review.setCustomerId(customerId);
        // Act
        reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(get("/reviews/customer/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].customerID").value(customerId.toString()))
            .andExpect(jsonPath("$[0].comment").value("Customer filter review"));
    }

    @Test
    void getReviewsByCustomer_nonExistingCustomer_returnsEmptyArray() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/reviews/customer/{customerId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByWine_existingWine_returnsMatchingReviews() throws Exception {
        // Arrange
        UUID wineId = UUID.randomUUID();
        Review review = createReview("Wine filter review");
        review.setWineId(wineId);
        // Act
        reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(get("/reviews/wine/{wineId}", wineId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].wineId").value(wineId.toString()))
            .andExpect(jsonPath("$[0].comment").value("Wine filter review"));
    }

    @Test
    void getReviewsByWine_nonExistingWine_returnsEmptyArray() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/reviews/wine/{wineId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createReview_validRequest_returnsCreatedReviewDto() throws Exception {
        // Arrange
        UUID wineId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        // Act + Assert
        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "wineId": "%s",
                      "customerID": "%s",
                      "rating": 4,
                      "comment": "Created review"
                    }
                    """.formatted(wineId, customerId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.customerID").value(customerId.toString()))
            .andExpect(jsonPath("$.rating").value(4))
            .andExpect(jsonPath("$.comment").value("Created review"));
    }

    @Test
    void createReview_invalidRequest_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "wineId": null,
                      "customerID": null,
                      "rating": 10,
                      "comment": "Invalid review"
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateReview_existingReview_returnsUpdatedReviewDto() throws Exception {
        // Arrange
        Review review = createReview("Before update");
        // Act
        review = reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(put("/reviews/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "wineId": "%s",
                      "customerID": "%s",
                      "rating": 3,
                      "comment": "After update"
                    }
                    """.formatted(review.getWineId(), review.getCustomerId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(review.getId().toString()))
            .andExpect(jsonPath("$.rating").value(3))
            .andExpect(jsonPath("$.comment").value("After update"));
    }

    @Test
    void updateReview_nonExistingReview_returnsNotFound() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();

        // Act + Assert
        mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "wineId": "%s",
                      "customerID": "%s",
                      "rating": 3,
                      "comment": "After update"
                    }
                    """.formatted(UUID.randomUUID(), UUID.randomUUID())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Review not found: " + reviewId));
    }

    @Test
    void deleteReview_existingReview_returnsNoContent() throws Exception {
        // Arrange
        Review review = createReview("Delete review");
        // Act
        review = reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(delete("/reviews/{id}", review.getId()))
            .andExpect(status().isNoContent());
    }

    private Review createReview(String comment) {
        Review review = new Review();
        review.setCustomerId(UUID.randomUUID());
        review.setWineId(UUID.randomUUID());
        review.setRating(5);
        review.setComment(comment);
        return review;
    }
}
