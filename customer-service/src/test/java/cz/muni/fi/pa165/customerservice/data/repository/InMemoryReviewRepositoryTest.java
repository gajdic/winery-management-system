package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryReviewRepositoryTest {

    private InMemoryReviewRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReviewRepository();
    }

    @Test
    void save_reviewWithoutId_assignsIdAndStoresReview() {
        Review review = createReview();

        Review savedReview = repository.save(review);

        assertThat(savedReview.getId()).isNotNull();
        assertThat(repository.findById(savedReview.getId())).contains(savedReview);
        assertThat(repository.findAll()).containsExactly(savedReview);
    }

    @Test
    void save_nullReview_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.save(null));

        assertThat(exception.getMessage()).isEqualTo("Review cannot be null");
    }

    @Test
    void save_reviewWithExistingId_updatesStoredReview() {
        UUID reviewId = UUID.randomUUID();
        Review originalReview = createReview();
        originalReview.setId(reviewId);
        repository.save(originalReview);

        Review updatedReview = createReview();
        updatedReview.setId(reviewId);
        updatedReview.setComment("Updated comment");
        updatedReview.setRating(3);

        Review savedReview = repository.save(updatedReview);

        assertThat(savedReview.getId()).isEqualTo(reviewId);
        assertThat(repository.findAll()).containsExactly(updatedReview);
        assertThat(repository.findById(reviewId)).contains(updatedReview);
    }

    @Test
    void save_reviewWithExistingId_preservesOnlyLatestValue() {
        UUID reviewId = UUID.randomUUID();
        Review originalReview = createReview();
        originalReview.setId(reviewId);
        repository.save(originalReview);

        Review updatedReview = createReview();
        updatedReview.setId(reviewId);
        updatedReview.setComment("Latest comment");
        updatedReview.setRating(2);

        repository.save(updatedReview);

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(reviewId)).contains(updatedReview);
    }

    @Test
    void findById_existingId_returnsReview() {
        Review savedReview = repository.save(createReview());

        var foundReview = repository.findById(savedReview.getId());

        assertThat(foundReview).contains(savedReview);
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        var foundReview = repository.findById(UUID.randomUUID());

        assertThat(foundReview).isEmpty();
    }

    @Test
    void findById_nullId_returnsEmptyOptional() {
        var foundReview = repository.findById(null);

        assertThat(foundReview).isEmpty();
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingReviews() {
        UUID customerId = UUID.randomUUID();
        Review matchingFirst = createReview(customerId, UUID.randomUUID(), 5, "Excellent");
        Review matchingSecond = createReview(customerId, UUID.randomUUID(), 4, "Very good");
        Review nonMatching = createReview(UUID.randomUUID(), UUID.randomUUID(), 2, "Average");

        Review savedFirst = repository.save(matchingFirst);
        Review savedSecond = repository.save(matchingSecond);
        repository.save(nonMatching);

        assertThat(repository.findByCustomerId(customerId))
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        repository.save(createReview());

        assertThat(repository.findByCustomerId(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsNullPointerException() {
        repository.save(createReview());

        assertThrows(NullPointerException.class, () -> repository.findByCustomerId(null));
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingReviews() {
        UUID wineId = UUID.randomUUID();
        Review matchingFirst = createReview(UUID.randomUUID(), wineId, 5, "Excellent");
        Review matchingSecond = createReview(UUID.randomUUID(), wineId, 4, "Very good");
        Review nonMatching = createReview(UUID.randomUUID(), UUID.randomUUID(), 2, "Average");

        Review savedFirst = repository.save(matchingFirst);
        Review savedSecond = repository.save(matchingSecond);
        repository.save(nonMatching);

        assertThat(repository.findByWineId(wineId))
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        repository.save(createReview());

        assertThat(repository.findByWineId(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByWineId_nullWineId_throwsNullPointerException() {
        repository.save(createReview());

        assertThrows(NullPointerException.class, () -> repository.findByWineId(null));
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findAll_multipleReviews_returnsAllReviews() {
        Review firstReview = createReview();
        Review secondReview = createReview(UUID.randomUUID(), UUID.randomUUID(), 1, "Poor");

        Review savedFirst = repository.save(firstReview);
        Review savedSecond = repository.save(secondReview);

        assertThat(repository.findAll())
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void deleteById_existingId_removesReview() {
        Review savedReview = repository.save(createReview());

        repository.deleteById(savedReview.getId());

        assertThat(repository.findById(savedReview.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingId_doesNothing() {
        Review savedReview = repository.save(createReview());

        repository.deleteById(UUID.randomUUID());

        assertThat(repository.findAll()).containsExactly(savedReview);
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
