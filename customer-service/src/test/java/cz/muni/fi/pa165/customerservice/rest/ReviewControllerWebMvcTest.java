package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.facade.ReviewFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewFacade reviewFacade;

    @Test
    void createReview_validRequest_returnsCreatedReview() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        UUID wineId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ReviewDto reviewDto = createReviewDto(reviewId, wineId, customerId, 4, "Created review");
        when(reviewFacade.createReview(any())).thenReturn(reviewDto);

        // Act + Assert
        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateReviewRequestJson(wineId, customerId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(reviewId.toString()))
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
                .content(invalidReviewRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getReview_existingId_returnsReview() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        UUID wineId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ReviewDto reviewDto = createReviewDto(reviewId, wineId, customerId, 5, "Excellent wine");
        when(reviewFacade.getReview(reviewId)).thenReturn(reviewDto);

        // Act + Assert
        mockMvc.perform(get("/reviews/{id}", reviewId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(reviewId.toString()))
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.customerID").value(customerId.toString()))
            .andExpect(jsonPath("$.rating").value(5))
            .andExpect(jsonPath("$.comment").value("Excellent wine"));
    }

    @Test
    void getReview_nonExistingId_returnsNotFound() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        when(reviewFacade.getReview(reviewId)).thenThrow(new IllegalArgumentException("Review not found: " + reviewId));

        // Act + Assert
        mockMvc.perform(get("/reviews/{id}", reviewId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Review not found: " + reviewId));
    }

    @Test
    void getAllReviews_returnsAllReviews() throws Exception {
        // Arrange
        ReviewDto firstReview = createReviewDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 5, "First review");
        ReviewDto secondReview = createReviewDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 3, "Second review");
        when(reviewFacade.getAllReviews()).thenReturn(List.of(firstReview, secondReview));

        // Act + Assert
        mockMvc.perform(get("/reviews"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(firstReview.getId().toString()))
            .andExpect(jsonPath("$[0].comment").value("First review"))
            .andExpect(jsonPath("$[1].id").value(secondReview.getId().toString()))
            .andExpect(jsonPath("$[1].comment").value("Second review"));
    }

    @Test
    void getAllReviews_emptyList_returnsEmptyArray() throws Exception {
        // Arrange
        when(reviewFacade.getAllReviews()).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/reviews"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByCustomer_existingCustomer_returnsMatchingReviews() throws Exception {
        // Arrange
        UUID customerId = UUID.randomUUID();
        ReviewDto review = createReviewDto(UUID.randomUUID(), UUID.randomUUID(), customerId, 4, "Customer review");
        when(reviewFacade.getReviewsByCustomer(customerId)).thenReturn(List.of(review));

        // Act + Assert
        mockMvc.perform(get("/reviews/customer/{customerId}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].customerID").value(customerId.toString()))
            .andExpect(jsonPath("$[0].comment").value("Customer review"));
    }

    @Test
    void getReviewsByCustomer_nonExistingCustomer_returnsEmptyArray() throws Exception {
        // Arrange
        UUID customerId = UUID.randomUUID();
        when(reviewFacade.getReviewsByCustomer(customerId)).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/reviews/customer/{customerId}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByWine_existingWine_returnsMatchingReviews() throws Exception {
        // Arrange
        UUID wineId = UUID.randomUUID();
        ReviewDto review = createReviewDto(UUID.randomUUID(), wineId, UUID.randomUUID(), 2, "Wine review");
        when(reviewFacade.getReviewsByWine(wineId)).thenReturn(List.of(review));

        // Act + Assert
        mockMvc.perform(get("/reviews/wine/{wineId}", wineId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].wineId").value(wineId.toString()))
            .andExpect(jsonPath("$[0].comment").value("Wine review"));
    }

    @Test
    void getReviewsByWine_nonExistingWine_returnsEmptyArray() throws Exception {
        // Arrange
        UUID wineId = UUID.randomUUID();
        when(reviewFacade.getReviewsByWine(wineId)).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/reviews/wine/{wineId}", wineId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void updateReview_validRequest_returnsUpdatedReview() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        UUID wineId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        ReviewDto updatedReview = createReviewDto(reviewId, wineId, customerId, 3, "After update");
        when(reviewFacade.updateReview(eq(reviewId), any())).thenReturn(updatedReview);

        // Act + Assert
        mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateReviewRequestJson(wineId, customerId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(reviewId.toString()))
            .andExpect(jsonPath("$.rating").value(3))
            .andExpect(jsonPath("$.comment").value("After update"));
    }

    @Test
    void updateReview_nonExistingId_returnsNotFound() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        when(reviewFacade.updateReview(eq(reviewId), any()))
            .thenThrow(new IllegalArgumentException("Review not found: " + reviewId));

        // Act + Assert
        mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateReviewRequestJson(UUID.randomUUID(), UUID.randomUUID())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Review not found: " + reviewId));
    }

    @Test
    void updateReview_invalidRequest_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(put("/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidReviewRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteReview_existingId_returnsNoContent() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        doNothing().when(reviewFacade).deleteReview(reviewId);

        // Act + Assert
        mockMvc.perform(delete("/reviews/{id}", reviewId))
            .andExpect(status().isNoContent());

        // Assert
        verify(reviewFacade).deleteReview(reviewId);
    }

    @Test
    void deleteReview_nonExistingId_returnsNotFound() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        doThrow(new IllegalArgumentException("Review not found: " + reviewId))
            .when(reviewFacade).deleteReview(reviewId);

        // Act + Assert
        mockMvc.perform(delete("/reviews/{id}", reviewId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Review not found: " + reviewId));
    }

    @Test
    void getReview_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/reviews/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateReview_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(put("/reviews/not-a-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateReviewRequestJson(UUID.randomUUID(), UUID.randomUUID())))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteReview_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(delete("/reviews/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsByCustomer_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/reviews/customer/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsByWine_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/reviews/wine/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getReview_unexpectedError_returnsInternalServerError() throws Exception {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        when(reviewFacade.getReview(reviewId)).thenThrow(new RuntimeException("Unexpected review failure"));

        // Act + Assert
        mockMvc.perform(get("/reviews/{id}", reviewId))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value("500 INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("Unexpected review failure"));
    }

    private ReviewDto createReviewDto(UUID id, UUID wineId, UUID customerId, int rating, String comment) {
        ReviewDto dto = new ReviewDto();
        dto.setId(id);
        dto.setWineId(wineId);
        dto.setCustomerID(customerId);
        dto.setRating(rating);
        dto.setComment(comment);
        return dto;
    }

    private String validCreateReviewRequestJson(UUID wineId, UUID customerId) {
        return """
            {
              "wineId": "%s",
              "customerID": "%s",
              "rating": 4,
              "comment": "Created review"
            }
            """.formatted(wineId, customerId);
    }

    private String validUpdateReviewRequestJson(UUID wineId, UUID customerId) {
        return """
            {
              "wineId": "%s",
              "customerID": "%s",
              "rating": 3,
              "comment": "After update"
            }
            """.formatted(wineId, customerId);
    }

    private String invalidReviewRequestJson() {
        return """
            {
              "wineId": null,
              "customerID": null,
              "rating": 10,
              "comment": "Invalid review"
            }
            """;
    }
}
