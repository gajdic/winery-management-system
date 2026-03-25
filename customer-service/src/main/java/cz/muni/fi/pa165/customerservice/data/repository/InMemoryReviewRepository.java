package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryReviewRepository implements ReviewRepository {

    private final Map<UUID, Review> storage = new HashMap<>();

    @Override
    public Review save(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        if (review.getId() == null) {
            review.setId(UUID.randomUUID());
        }
        storage.put(review.getId(), review);
        return review;
    }

    @Override
    public Optional<Review> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Review> findByCustomerId(UUID customerId) {
        return storage.values().stream()
            .filter(r -> customerId.equals(r.getCustomerId())).toList();
    }

    @Override
    public List<Review> findByWineId(UUID wineId) {
        return storage.values().stream()
            .filter(r -> wineId.equals(r.getWineId())).toList();
    }

    @Override
    public List<Review> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
