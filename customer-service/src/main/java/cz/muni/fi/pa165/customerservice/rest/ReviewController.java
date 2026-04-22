package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.facade.ReviewFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Customer review management endpoints")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    public ReviewController(ReviewFacade reviewFacade) {
        this.reviewFacade = reviewFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create review",
        description = "Creates a new review for a wine by a customer."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Review created",
            content = @Content(schema = @Schema(implementation = ReviewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ReviewDto> createReview(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Review creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewCreateDto.class))
        )
        @RequestBody ReviewCreateDto dto
    ) {
        ReviewDto created = reviewFacade.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get review by ID",
        description = "Returns a review identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Review found",
            content = @Content(schema = @Schema(implementation = ReviewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Review not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ReviewDto> getReview(
        @Parameter(description = "Review ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(reviewFacade.getReview(id));
    }

    @GetMapping
    @Operation(
        summary = "Get all reviews",
        description = "Returns all reviews."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reviews retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class))))
    })
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewFacade.getAllReviews());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(
        summary = "Get reviews by customer",
        description = "Returns all reviews created by a specific customer."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reviews retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class)))),
        @ApiResponse(responseCode = "404", description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ReviewDto>> getByCustomer(
        @Parameter(description = "Customer ID", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID customerId
    ) {
        return ResponseEntity.ok(reviewFacade.getReviewsByCustomer(customerId));
    }

    @GetMapping("/wine/{wineId}")
    @Operation(
        summary = "Get reviews by wine",
        description = "Returns all reviews for a specific wine."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reviews retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class)))),
        @ApiResponse(responseCode = "404", description = "Wine not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ReviewDto>> getByWine(
        @Parameter(description = "Wine ID", example = "33333333-3333-3333-3333-333333333333")
        @PathVariable UUID wineId
    ) {
        return ResponseEntity.ok(reviewFacade.getReviewsByWine(wineId));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update review",
        description = "Updates an existing review."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Review updated",
            content = @Content(schema = @Schema(implementation = ReviewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Review not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ReviewDto> updateReview(
        @Parameter(description = "Review ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id,
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Review update request",
            required = true,
            content = @Content(schema = @Schema(implementation = ReviewUpdateDto.class))
        )
        @RequestBody ReviewUpdateDto dto
    ) {
        return ResponseEntity.ok(reviewFacade.updateReview(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete review",
        description = "Deletes a review by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Review deleted"),
        @ApiResponse(responseCode = "404", description = "Review not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteReview(
        @Parameter(description = "Review ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        reviewFacade.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
