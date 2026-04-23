package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
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

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save_validReview_reviewIsPersisted() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customerRepository.save(customer);

        Review review = TestDataFactory.reviewEntity();
        review.setCustomer(customer);

        // Act
        Review saved = reviewRepository.save(review);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRating()).isEqualTo(5);
    }

    @Test
    void findById_existingReview_returnsReview() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customerRepository.save(customer);

        Review review = TestDataFactory.reviewEntity();
        review.setCustomer(customer);

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
        Customer customer = TestDataFactory.customerEntity();
        customerRepository.save(customer);

        Review review = TestDataFactory.reviewEntity();
        review.setCustomer(customer);

        Review saved = reviewRepository.save(review);

        // Act
        reviewRepository.deleteById(saved.getId());

        // Assert
        assertThat(reviewRepository.findById(saved.getId()))
            .isEmpty();
    }

    @Test
    void findByCustomerId_existingCustomer_returnsReviews() {
        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());
        Customer otherCustomer = customerRepository.save(TestDataFactory.customerEntity());

        UUID customerId = customer.getId();

        Review r1 = TestDataFactory.reviewEntity();
        r1.setCustomer(customer);

        Review r2 = TestDataFactory.reviewEntity();
        r2.setCustomer(customer);

        Review r3 = TestDataFactory.reviewEntity();
        r3.setCustomer(otherCustomer);

        reviewRepository.saveAllAndFlush(List.of(r1, r2, r3));

        // Act
        List<Review> result = reviewRepository.findByCustomer_Id(customerId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomer().getId().equals(customerId));
    }

    @Test
    void findByWineId_existingWine_returnsReviews() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customer = customerRepository.save(customer);

        UUID wineId = UUID.randomUUID();

        Review r1 = TestDataFactory.reviewEntity();
        r1.setCustomer(customer);
        r1.setWineId(wineId);

        Review r2 = TestDataFactory.reviewEntity();
        r2.setCustomer(customer);
        r2.setWineId(wineId);

        Review r3 = TestDataFactory.reviewEntity();
        r3.setCustomer(customer);
        r3.setWineId(UUID.randomUUID());

        reviewRepository.saveAll(List.of(r1, r2, r3));

        // Act
        List<Review> result = reviewRepository.findByWineId(wineId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId));
    }
}
