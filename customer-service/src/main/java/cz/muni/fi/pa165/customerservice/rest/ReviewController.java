package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.facade.ReviewFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewCreateDto dto) {
        ReviewDto created = reviewFacade.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewFacade.getReview(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewFacade.getAllReviews());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewDto>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(reviewFacade.getReviewsByCustomer(customerId));
    }

    @GetMapping("/wine/{wineId}")
    public ResponseEntity<List<ReviewDto>> getByWine(@PathVariable UUID wineId) {
        return ResponseEntity.ok(reviewFacade.getReviewsByWine(wineId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(
        @PathVariable UUID id,
        @Valid @RequestBody ReviewUpdateDto dto) {

        return ResponseEntity.ok(reviewFacade.updateReview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewFacade.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
