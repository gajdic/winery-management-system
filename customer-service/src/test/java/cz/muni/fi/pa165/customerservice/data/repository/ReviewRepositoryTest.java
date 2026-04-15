package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void save_validReview_reviewIsPersisted() {
        // Arrange
        Review review = TestDataFactory.reviewEntity();

        // Act
        Review saved = reviewRepository.save(review);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRating()).isEqualTo(5);
    }

    @Test
    void findById_existingReview_returnsReview() {
        // Arrange
        Review review = TestDataFactory.reviewEntity();
        Review saved = reviewRepository.save(review);

        // Act
        var result = reviewRepository.findById(saved.getId());

        // Assert
        assertThat(result).isPresent();
    }

    @Test
    void save_ratingBelowMin_throwsConstraintViolationException() {
        // Arrange
        Review review = TestDataFactory.reviewEntity();
        review.setRating(-1);

        // Act & Assert
        assertThatThrownBy(() ->
            reviewRepository.saveAndFlush(review)
        ).isInstanceOf(Exception.class);
    }

    @Test
    void save_ratingAboveMax_throwsConstraintViolationException() {
        // Arrange
        Review review = TestDataFactory.reviewEntity();
        review.setRating(10);

        // Act & Assert
        assertThatThrownBy(() ->
            reviewRepository.saveAndFlush(review)
        ).isInstanceOf(Exception.class);
    }

    @Test
    void deleteById_existingReview_reviewIsDeleted() {
        // Arrange
        Review saved = reviewRepository.save(TestDataFactory.reviewEntity());

        // Act
        reviewRepository.deleteById(saved.getId());

        // Assert
        assertThat(reviewRepository.findById(saved.getId()))
            .isEmpty();
    }

    @Test
    void findByCustomerId_existingCustomer_returnsReviews() {
        // Arrange
        UUID customerId = UUID.randomUUID();

        Review r1 = TestDataFactory.reviewEntity();
        r1.setCustomerId(customerId);

        Review r2 = TestDataFactory.reviewEntity();
        r2.setCustomerId(customerId);

        Review r3 = TestDataFactory.reviewEntity();
        r3.setCustomerId(UUID.randomUUID());

        reviewRepository.save(r1);
        reviewRepository.save(r2);
        reviewRepository.save(r3);

        // Act
        List<Review> result = reviewRepository.findByCustomerId(customerId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomerId().equals(customerId));
    }

    @Test
    void findByWineId_existingWine_returnsReviews() {
        // Arrange
        UUID wineId = UUID.randomUUID();

        Review r1 = TestDataFactory.reviewEntity();
        r1.setWineId(wineId);

        Review r2 = TestDataFactory.reviewEntity();
        r2.setWineId(wineId);

        Review r3 = TestDataFactory.reviewEntity();
        r3.setWineId(UUID.randomUUID());

        reviewRepository.save(r1);
        reviewRepository.save(r2);
        reviewRepository.save(r3);

        // Act
        List<Review> result = reviewRepository.findByWineId(wineId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId));
    }
}
