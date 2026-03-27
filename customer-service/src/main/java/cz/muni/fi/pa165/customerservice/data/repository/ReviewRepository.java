package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(UUID id);

    List<Review> findByCustomerId(UUID customerId);

    List<Review> findByWineId(UUID wineId);

    List<Review> findAll();

    void deleteById(UUID id);

}
