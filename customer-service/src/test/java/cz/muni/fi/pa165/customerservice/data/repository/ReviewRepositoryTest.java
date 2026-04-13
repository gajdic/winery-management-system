package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;
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
        Review review = createReview();

        Review saved = reviewRepository.save(review);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRating()).isEqualTo(4);
    }

    @Test
    void findById_existingReview_returnsReview() {
        Review saved = reviewRepository.save(createReview());

        assertThat(reviewRepository.findById(saved.getId()))
            .isPresent();
    }
    @Test
    void save_ratingBelowMin_throwsConstraintViolationException() {
        Review review = createReview();
        review.setRating(-1);

        assertThatThrownBy(() -> {
            reviewRepository.saveAndFlush(review);
        }).isInstanceOf(Exception.class);
    }

    @Test
    void save_ratingAboveMax_throwsConstraintViolationException() {
        Review review = createReview();
        review.setRating(10);

        assertThatThrownBy(() -> {
            reviewRepository.saveAndFlush(review);
        }).isInstanceOf(Exception.class);
    }


    @Test
    void deleteById_existingReview_reviewIsDeleted() {
        Review saved = reviewRepository.save(createReview());

        reviewRepository.deleteById(saved.getId());

        assertThat(reviewRepository.findById(saved.getId()))
            .isEmpty();
    }

    @Test
    void findByCustomerId_existingCustomer_returnsReviews() {
        UUID customerId = UUID.randomUUID();

        Review r1 = createReview();
        r1.setCustomerId(customerId);

        Review r2 = createReview();
        r2.setCustomerId(customerId);

        Review r3 = createReview();
        r3.setCustomerId(UUID.randomUUID());

        reviewRepository.save(r1);
        reviewRepository.save(r2);
        reviewRepository.save(r3);

        List<Review> result = reviewRepository.findByCustomerId(customerId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomerId().equals(customerId));
    }

    @Test
    void findByWineId_existingWine_returnsReviews() {
        UUID wineId = UUID.randomUUID();

        Review r1 = createReview();
        r1.setWineId(wineId);

        Review r2 = createReview();
        r2.setWineId(wineId);

        Review r3 = createReview();
        r3.setWineId(UUID.randomUUID());

        reviewRepository.save(r1);
        reviewRepository.save(r2);
        reviewRepository.save(r3);

        List<Review> result = reviewRepository.findByWineId(wineId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId));
    }


    private Review createReview() {
        Review review = new Review();
        review.setCustomerId(UUID.randomUUID());
        review.setWineId(UUID.randomUUID());
        review.setRating(4);
        review.setComment("Nice wine");
        return review;
    }
}
