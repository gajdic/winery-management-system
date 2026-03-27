package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import cz.muni.fi.pa165.customerservice.data.repository.InMemoryCustomerRepository;
import cz.muni.fi.pa165.customerservice.data.repository.InMemoryReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private InMemoryReviewRepository reviewRepository;

    public Review save(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        if (review.getRating() < 0 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        if (review.getCustomerId() == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }
        if (review.getWineId() == null) {
            throw new IllegalArgumentException("WineId cannot be null");
        }

        return reviewRepository.save(review);
    }

    public Review update(UUID id, Review updatedData) {
        if (updatedData == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        if (updatedData.getRating() < 0 || updatedData.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        if (updatedData.getCustomerId() == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }
        if (updatedData.getWineId() == null) {
            throw new IllegalArgumentException("WineId cannot be null");
        }

        Review existing = findById(id);

        existing.setRating(updatedData.getRating());
        existing.setComment(updatedData.getComment());

        return reviewRepository.save(existing);
    }

    public Review findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Review id cannot be null");
        }
        return reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
    }

    public List<Review> findByCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }
        return reviewRepository.findByCustomerId(customerId);
    }

    public List<Review> findByWineId(UUID wineId) {
        if (wineId == null) {
            throw new IllegalArgumentException("WineId cannot be null");
        }
        return reviewRepository.findByWineId(wineId);
    }

    public Collection<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void deleteById(UUID id) {
        reviewRepository.deleteById(id);
    }
}
