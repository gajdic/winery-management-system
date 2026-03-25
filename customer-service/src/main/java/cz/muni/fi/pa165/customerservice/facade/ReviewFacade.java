package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.mappers.ReviewMapper;
import cz.muni.fi.pa165.customerservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewFacade {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewFacade(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    public ReviewDto createReview(ReviewCreateDto dto) {
        Review review = reviewMapper.mapToEntity(dto);
        Review saved = reviewService.save(review);
        return reviewMapper.mapToDto(saved);
    }

    public ReviewDto getReview(UUID id) {
        Review review = reviewService.findById(id);
        return reviewMapper.mapToDto(review);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewService.findAll()
            .stream()
            .map(reviewMapper::mapToDto)
            .collect(Collectors.toList());
    }
    public List<ReviewDto> getReviewsByCustomer(UUID customerId) {
        return reviewService.findByCustomerId(customerId)
            .stream()
            .map(reviewMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByWine(UUID wineId) {
        return reviewService.findByWineId(wineId)
            .stream()
            .map(reviewMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public ReviewDto updateReview(UUID id, ReviewUpdateDto dto) {
        Review review = reviewService.findById(id);
        reviewMapper.updateEntityFromDto(dto, review);
        Review updated = reviewService.save(review);
        return reviewMapper.mapToDto(updated);
    }

    public void deleteReview(UUID id) {
        reviewService.deleteById(id);
    }
}
