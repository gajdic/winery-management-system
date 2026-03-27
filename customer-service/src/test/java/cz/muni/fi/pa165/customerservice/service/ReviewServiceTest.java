package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.repository.InMemoryReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private InMemoryReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void save_validReview_callsRepositoryAndReturnsSavedReview() {
        Review review = createReview();
        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        Review savedReview = reviewService.save(review);

        assertThat(savedReview).isEqualTo(review);
        Mockito.verify(reviewRepository).save(review);
    }

    @Test
    void save_nullReview_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(null));

        assertThat(exception.getMessage()).isEqualTo("Review cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_ratingBelowZero_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setRating(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_ratingAboveFive_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setRating(6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullCustomerId_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setCustomerId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullWineId_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setWineId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_validReview_updatesRatingAndCommentAndReturnsSavedReview() {
        UUID reviewId = UUID.randomUUID();
        UUID originalCustomerId = UUID.randomUUID();
        UUID originalWineId = UUID.randomUUID();

        Review existingReview = createReview(originalCustomerId, originalWineId, 5, "Original comment");
        existingReview.setId(reviewId);

        Review updatedData = createReview(UUID.randomUUID(), UUID.randomUUID(), 3, "Updated comment");

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        Mockito.when(reviewRepository.save(existingReview)).thenReturn(existingReview);

        Review updatedReview = reviewService.update(reviewId, updatedData);

        assertThat(updatedReview.getId()).isEqualTo(reviewId);
        assertThat(updatedReview.getRating()).isEqualTo(3);
        assertThat(updatedReview.getComment()).isEqualTo("Updated comment");
        assertThat(updatedReview.getCustomerId()).isEqualTo(originalCustomerId);
        assertThat(updatedReview.getWineId()).isEqualTo(originalWineId);
        Mockito.verify(reviewRepository).findById(reviewId);
        Mockito.verify(reviewRepository).save(existingReview);
    }

    @Test
    void update_nullReview_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), null));

        assertThat(exception.getMessage()).isEqualTo("Review cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullId_throwsIllegalArgumentException() {
        Review review = createReview();

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(null, review));

        assertThat(exception.getMessage()).isEqualTo("Review id cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_ratingBelowZero_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setRating(-1);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_ratingAboveFive_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setRating(6);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullCustomerId_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setCustomerId(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullWineId_throwsIllegalArgumentException() {
        Review review = createReview();
        review.setWineId(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findById_existingId_returnsReview() {
        UUID reviewId = UUID.randomUUID();
        Review review = createReview();
        review.setId(reviewId);
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review foundReview = reviewService.findById(reviewId);

        assertThat(foundReview).isEqualTo(review);
        Mockito.verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findById_nullId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.findById(null));

        assertThat(exception.getMessage()).isEqualTo("Review id cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void findById_nonExistingId_throwsIllegalArgumentException() {
        UUID reviewId = UUID.randomUUID();
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.findById(reviewId));

        assertThat(exception.getMessage()).isEqualTo("Review not found: " + reviewId);
        Mockito.verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingReviews() {
        UUID customerId = UUID.randomUUID();
        List<Review> reviews = List.of(createReview(customerId, UUID.randomUUID(), 5, "Excellent"));
        Mockito.when(reviewRepository.findByCustomerId(customerId)).thenReturn(reviews);

        List<Review> foundReviews = reviewService.findByCustomerId(customerId);

        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(reviewRepository.findByCustomerId(customerId)).thenReturn(List.of());

        List<Review> foundReviews = reviewService.findByCustomerId(customerId);

        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.findByCustomerId(null));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findByCustomerId(Mockito.any());
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingReviews() {
        UUID wineId = UUID.randomUUID();
        List<Review> reviews = List.of(createReview(UUID.randomUUID(), wineId, 4, "Very good"));
        Mockito.when(reviewRepository.findByWineId(wineId)).thenReturn(reviews);

        List<Review> foundReviews = reviewService.findByWineId(wineId);

        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        UUID wineId = UUID.randomUUID();
        Mockito.when(reviewRepository.findByWineId(wineId)).thenReturn(List.of());

        List<Review> foundReviews = reviewService.findByWineId(wineId);

        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nullWineId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> reviewService.findByWineId(null));

        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findByWineId(Mockito.any());
    }

    @Test
    void findAll_returnsAllReviews() {
        List<Review> reviews = List.of(
            createReview(UUID.randomUUID(), UUID.randomUUID(), 5, "Excellent"),
            createReview(UUID.randomUUID(), UUID.randomUUID(), 3, "Average")
        );
        Mockito.when(reviewRepository.findAll()).thenReturn(reviews);

        var foundReviews = reviewService.findAll();

        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of());

        var foundReviews = reviewService.findAll();

        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        UUID reviewId = UUID.randomUUID();

        reviewService.deleteById(reviewId);

        Mockito.verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void deleteById_nullId_callsRepositoryDeleteByIdWithNull() {
        reviewService.deleteById(null);

        Mockito.verify(reviewRepository).deleteById(null);
    }

    @Test
    void deleteById_nonExistingId_stillCallsRepositoryDeleteById() {
        UUID reviewId = UUID.randomUUID();

        reviewService.deleteById(reviewId);

        Mockito.verify(reviewRepository).deleteById(reviewId);
    }

    private Review createReview() {
        return createReview(UUID.randomUUID(), UUID.randomUUID(), 5, "Great wine");
    }

    private Review createReview(UUID customerId, UUID wineId, int rating, String comment) {
        Review review = new Review();
        review.setCustomerId(customerId);
        review.setWineId(wineId);
        review.setRating(rating);
        review.setComment(comment);
        return review;
    }
}
