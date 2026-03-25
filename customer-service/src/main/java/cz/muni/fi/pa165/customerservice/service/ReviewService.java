package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.Review;
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
        return reviewRepository.save(review);
    }

    public Review findById(UUID id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findByCustomerId(UUID customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    public List<Review> findByWineId(UUID wineId) {
        return reviewRepository.findByWineId(wineId);
    }

    public Collection<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void deleteById(UUID id) {
        reviewRepository.deleteById(id);
    }
}
