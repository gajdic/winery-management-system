package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.facade.ReviewFacade;
import cz.muni.fi.pa165.customerservice.mappers.ReviewMapper;
import cz.muni.fi.pa165.customerservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    public ReviewController(ReviewFacade reviewFacade) {
        this.reviewFacade = reviewFacade;
    }

    @PostMapping
    public ReviewDto createReview(@Valid @RequestBody ReviewCreateDto dto) {
        return reviewFacade.createReview(dto);
    }

    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable UUID id) {
        return reviewFacade.getReview(id);
    }

    @GetMapping
    public List<ReviewDto> getAllReviews() {
        return reviewFacade.getAllReviews();
    }

    @GetMapping("/customer/{customerId}")
    public List<ReviewDto> getByCustomer(@PathVariable UUID customerId) {
        return reviewFacade.getReviewsByCustomer(customerId);
    }

    @GetMapping("/wine/{wineId}")
    public List<ReviewDto> getByWine(@PathVariable UUID wineId) {
        return reviewFacade.getReviewsByWine(wineId);
    }

    @PutMapping("/{id}")
    public ReviewDto updateReview(@PathVariable UUID id,
                                  @Valid @RequestBody ReviewUpdateDto dto) {
        return reviewFacade.updateReview(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable UUID id) {
        reviewFacade.deleteReview(id);
    }
}
