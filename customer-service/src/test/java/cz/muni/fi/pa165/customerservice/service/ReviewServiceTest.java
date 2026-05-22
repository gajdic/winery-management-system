package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.ReviewRepository;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
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
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void save_validReview_callsRepositoryAndReturnsSavedReview() {
        // Arrange
        Review review = createReview();
        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        // Act
        Review savedReview = reviewService.save(review);

        // Assert
        assertThat(savedReview).isEqualTo(review);
        Mockito.verify(reviewRepository).save(review);
    }

    @Test
    void save_nullReview_throwsIllegalArgumentException() {
        // Arrange + Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_ratingBelowZero_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setRating(-1);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_ratingAboveFive_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setRating(6);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullCustomerId_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setCustomer(null);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullWineId_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setWineId(null);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.save(review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_validReview_updatesRatingAndCommentAndReturnsSavedReview() {
        // Arrange
        UUID reviewId = UUID.randomUUID();

        Customer customer = TestDataFactory.customerEntity();
        customer.setId(UUID.randomUUID());

        UUID wineId = UUID.randomUUID();

        Review existingReview = createReview(customer, wineId, 5, "Original comment");
        existingReview.setId(reviewId);

        Review updatedData = createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 3, "Updated comment");

        Mockito.when(reviewRepository.findById(reviewId))
            .thenReturn(Optional.of(existingReview));

        Mockito.when(reviewRepository.save(existingReview))
            .thenReturn(existingReview);

        // Act
        Review updatedReview = reviewService.update(reviewId, updatedData);

        // Assert
        assertThat(updatedReview.getId()).isEqualTo(reviewId);
        assertThat(updatedReview.getRating()).isEqualTo(3);
        assertThat(updatedReview.getComment()).isEqualTo("Updated comment");

        assertThat(updatedReview.getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(updatedReview.getWineId()).isEqualTo(wineId);

        Mockito.verify(reviewRepository).findById(reviewId);
        Mockito.verify(reviewRepository).save(existingReview);
    }

    @Test
    void update_nullReview_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullId_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(null, review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review id cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_ratingBelowZero_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setRating(-1);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_ratingAboveFive_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setRating(6);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 0 and 5");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullCustomer_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setCustomer(null);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullWineId_throwsIllegalArgumentException() {
        // Arrange
        Review review = createReview();
        review.setWineId(null);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.update(UUID.randomUUID(), review));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findById_existingId_returnsReview() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Review review = createReview();
        review.setId(reviewId);
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        Review foundReview = reviewService.findById(reviewId);

        // Assert
        assertThat(foundReview).isEqualTo(review);
        Mockito.verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findById_nullId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.findById(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review id cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void findById_nonExistingId_throwsResourceNotFoundException() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> reviewService.findById(reviewId));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review not found: " + reviewId);
        Mockito.verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingReviews() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        List<Review> reviews = List.of(createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 5, "Excellent"));
        Mockito.when(reviewRepository.findByCustomer_Id(customerId)).thenReturn(reviews);

        // Act
        List<Review> foundReviews = reviewService.findByCustomerId(customerId);

        // Assert
        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findByCustomer_Id(customerId);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Mockito.when(reviewRepository.findByCustomer_Id(customerId)).thenReturn(List.of());

        // Act
        List<Review> foundReviews = reviewService.findByCustomerId(customerId);

        // Assert
        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findByCustomer_Id(customerId);
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.findByCustomerId(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findByCustomer_Id(Mockito.any());
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingReviews() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        List<Review> reviews = List.of(createReview(TestDataFactory.customerEntity(), wineId, 4, "Very good"));
        Mockito.when(reviewRepository.findByWineId(wineId)).thenReturn(reviews);

        // Act
        List<Review> foundReviews = reviewService.findByWineId(wineId);

        // Assert
        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        Mockito.when(reviewRepository.findByWineId(wineId)).thenReturn(List.of());

        // Act
        List<Review> foundReviews = reviewService.findByWineId(wineId);

        // Assert
        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nullWineId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.findByWineId(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findByWineId(Mockito.any());
    }

    @Test
    void findAll_returnsAllReviews() {
        // Arrange
        List<Review> reviews = List.of(
            createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 5, "Excellent"),
            createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 3, "Average")
        );
        Mockito.when(reviewRepository.findAll()).thenReturn(reviews);

        // Act
        var foundReviews = reviewService.findAll();

        // Assert
        assertThat(foundReviews).isEqualTo(reviews);
        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // Arrange
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of());

        // Act
        var foundReviews = reviewService.findAll();

        // Assert
        assertThat(foundReviews).isEmpty();
        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Review review = createReview();
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        reviewService.deleteById(reviewId);

        // Assert
        Mockito.verify(reviewRepository).findById(reviewId);
        Mockito.verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> reviewService.deleteById(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review id cannot be null");
        Mockito.verify(reviewRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(reviewRepository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void deleteById_nonExistingId_throwsResourceNotFoundException() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteById(reviewId));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Review not found: " + reviewId);
        Mockito.verify(reviewRepository).findById(reviewId);
        Mockito.verify(reviewRepository, Mockito.never()).deleteById(Mockito.any());
    }

    private Review createReview() {
        return createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 5, "Great wine");
    }

    private Review createReview(Customer customer, UUID wineId, int rating, String comment) {
        Review review = new Review();
        review.setCustomer(customer);
        review.setWineId(wineId);
        review.setRating(rating);
        review.setComment(comment);
        return review;
    }
}
