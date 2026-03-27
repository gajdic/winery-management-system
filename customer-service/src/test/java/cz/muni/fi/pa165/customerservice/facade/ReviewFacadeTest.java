package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.mappers.ReviewMapper;
import cz.muni.fi.pa165.customerservice.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ReviewFacadeTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewFacade reviewFacade;

    @Test
    void createReview_validDto_mapsSavesAndReturnsDto() {
        ReviewCreateDto createDto = createReviewCreateDto();
        Review review = createReview();
        Review savedReview = createReview();
        savedReview.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(savedReview.getId(), savedReview.getWineId(), savedReview.getCustomerId(), savedReview.getRating(), savedReview.getComment());

        Mockito.when(reviewMapper.mapToEntity(createDto)).thenReturn(review);
        Mockito.when(reviewService.save(review)).thenReturn(savedReview);
        Mockito.when(reviewMapper.mapToDto(savedReview)).thenReturn(reviewDto);

        ReviewDto createdReview = reviewFacade.createReview(createDto);

        assertThat(createdReview).isEqualTo(reviewDto);
        Mockito.verify(reviewMapper).mapToEntity(createDto);
        Mockito.verify(reviewService).save(review);
        Mockito.verify(reviewMapper).mapToDto(savedReview);
    }

    @Test
    void getReview_existingId_returnsMappedDto() {
        UUID reviewId = UUID.randomUUID();
        Review review = createReview();
        review.setId(reviewId);
        ReviewDto reviewDto = createReviewDto(reviewId, review.getWineId(), review.getCustomerId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findById(reviewId)).thenReturn(review);
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        ReviewDto foundReview = reviewFacade.getReview(reviewId);

        assertThat(foundReview).isEqualTo(reviewDto);
        Mockito.verify(reviewService).findById(reviewId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getAllReviews_returnsMappedDtos() {
        Review firstReview = createReview();
        firstReview.setId(UUID.randomUUID());
        Review secondReview = createReview(UUID.randomUUID(), UUID.randomUUID(), 3, "Average");
        secondReview.setId(UUID.randomUUID());

        ReviewDto firstDto = createReviewDto(firstReview.getId(), firstReview.getWineId(), firstReview.getCustomerId(), firstReview.getRating(), firstReview.getComment());
        ReviewDto secondDto = createReviewDto(secondReview.getId(), secondReview.getWineId(), secondReview.getCustomerId(), secondReview.getRating(), secondReview.getComment());

        Mockito.when(reviewService.findAll()).thenReturn(List.of(firstReview, secondReview));
        Mockito.when(reviewMapper.mapToDto(firstReview)).thenReturn(firstDto);
        Mockito.when(reviewMapper.mapToDto(secondReview)).thenReturn(secondDto);

        List<ReviewDto> reviews = reviewFacade.getAllReviews();

        assertThat(reviews).containsExactly(firstDto, secondDto);
        Mockito.verify(reviewService).findAll();
        Mockito.verify(reviewMapper).mapToDto(firstReview);
        Mockito.verify(reviewMapper).mapToDto(secondReview);
    }

    @Test
    void getAllReviews_emptyList_returnsEmptyListWithoutMapping() {
        Mockito.when(reviewService.findAll()).thenReturn(List.of());

        List<ReviewDto> reviews = reviewFacade.getAllReviews();

        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findAll();
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void getReviewsByCustomer_existingCustomerId_returnsMappedDtos() {
        UUID customerId = UUID.randomUUID();
        Review review = createReview(customerId, UUID.randomUUID(), 5, "Excellent");
        review.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(review.getId(), review.getWineId(), review.getCustomerId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findByCustomerId(customerId)).thenReturn(List.of(review));
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        List<ReviewDto> reviews = reviewFacade.getReviewsByCustomer(customerId);

        assertThat(reviews).containsExactly(reviewDto);
        Mockito.verify(reviewService).findByCustomerId(customerId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getReviewsByCustomer_nonExistingCustomerId_returnsEmptyListWithoutMapping() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(reviewService.findByCustomerId(customerId)).thenReturn(List.of());

        List<ReviewDto> reviews = reviewFacade.getReviewsByCustomer(customerId);

        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findByCustomerId(customerId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void getReviewsByWine_existingWineId_returnsMappedDtos() {
        UUID wineId = UUID.randomUUID();
        Review review = createReview(UUID.randomUUID(), wineId, 4, "Very good");
        review.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(review.getId(), review.getWineId(), review.getCustomerId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findByWineId(wineId)).thenReturn(List.of(review));
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        List<ReviewDto> reviews = reviewFacade.getReviewsByWine(wineId);

        assertThat(reviews).containsExactly(reviewDto);
        Mockito.verify(reviewService).findByWineId(wineId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getReviewsByWine_nonExistingWineId_returnsEmptyListWithoutMapping() {
        UUID wineId = UUID.randomUUID();
        Mockito.when(reviewService.findByWineId(wineId)).thenReturn(List.of());

        List<ReviewDto> reviews = reviewFacade.getReviewsByWine(wineId);

        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findByWineId(wineId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void updateReview_validDto_mapsUpdatesAndReturnsDto() {
        UUID reviewId = UUID.randomUUID();
        ReviewUpdateDto updateDto = createReviewUpdateDto();
        Review mappedReview = createReview(updateDto.getCustomerID(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());
        mappedReview.setId(reviewId);
        Review updatedReview = createReview(updateDto.getCustomerID(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());
        updatedReview.setId(reviewId);
        ReviewDto reviewDto = createReviewDto(reviewId, updatedReview.getWineId(), updatedReview.getCustomerId(), updatedReview.getRating(), updatedReview.getComment());

        Mockito.when(reviewMapper.mapToEntity(reviewId, updateDto)).thenReturn(mappedReview);
        Mockito.when(reviewService.update(reviewId, mappedReview)).thenReturn(updatedReview);
        Mockito.when(reviewMapper.mapToDto(updatedReview)).thenReturn(reviewDto);

        ReviewDto result = reviewFacade.updateReview(reviewId, updateDto);

        assertThat(result).isEqualTo(reviewDto);
        Mockito.verify(reviewMapper).mapToEntity(reviewId, updateDto);
        Mockito.verify(reviewService).update(reviewId, mappedReview);
        Mockito.verify(reviewMapper).mapToDto(updatedReview);
    }

    @Test
    void createReview_serviceThrowsException_propagatesException() {
        ReviewCreateDto createDto = createReviewCreateDto();
        Review review = createReview();

        Mockito.when(reviewMapper.mapToEntity(createDto)).thenReturn(review);
        Mockito.when(reviewService.save(review)).thenThrow(new IllegalArgumentException("Review cannot be null"));

        assertThatThrownBy(() -> reviewFacade.createReview(createDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Review cannot be null");

        Mockito.verify(reviewMapper).mapToEntity(createDto);
        Mockito.verify(reviewService).save(review);
        Mockito.verify(reviewMapper, Mockito.never()).mapToDto(Mockito.any());
    }

    @Test
    void getReview_serviceThrowsException_propagatesException() {
        UUID reviewId = UUID.randomUUID();
        Mockito.when(reviewService.findById(reviewId)).thenThrow(new IllegalArgumentException("Review not found: " + reviewId));

        assertThatThrownBy(() -> reviewFacade.getReview(reviewId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Review not found: " + reviewId);

        Mockito.verify(reviewService).findById(reviewId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void updateReview_serviceThrowsException_propagatesException() {
        UUID reviewId = UUID.randomUUID();
        ReviewUpdateDto updateDto = createReviewUpdateDto();
        Review mappedReview = createReview(updateDto.getCustomerID(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());

        Mockito.when(reviewMapper.mapToEntity(reviewId, updateDto)).thenReturn(mappedReview);
        Mockito.when(reviewService.update(reviewId, mappedReview))
            .thenThrow(new IllegalArgumentException("Review not found: " + reviewId));

        assertThatThrownBy(() -> reviewFacade.updateReview(reviewId, updateDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Review not found: " + reviewId);

        Mockito.verify(reviewMapper).mapToEntity(reviewId, updateDto);
        Mockito.verify(reviewService).update(reviewId, mappedReview);
        Mockito.verify(reviewMapper, Mockito.never()).mapToDto(Mockito.any());
    }

    @Test
    void deleteReview_callsServiceDeleteById() {
        UUID reviewId = UUID.randomUUID();

        reviewFacade.deleteReview(reviewId);

        Mockito.verify(reviewService).deleteById(reviewId);
    }

    @Test
    void deleteReview_serviceThrowsException_propagatesException() {
        UUID reviewId = UUID.randomUUID();
        Mockito.doThrow(new IllegalArgumentException("Review not found: " + reviewId))
            .when(reviewService).deleteById(reviewId);

        assertThatThrownBy(() -> reviewFacade.deleteReview(reviewId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Review not found: " + reviewId);

        Mockito.verify(reviewService).deleteById(reviewId);
    }

    private ReviewCreateDto createReviewCreateDto() {
        ReviewCreateDto dto = new ReviewCreateDto();
        dto.setWineId(UUID.randomUUID());
        dto.setCustomerID(UUID.randomUUID());
        dto.setRating(5);
        dto.setComment("Great wine");
        return dto;
    }

    private ReviewUpdateDto createReviewUpdateDto() {
        ReviewUpdateDto dto = new ReviewUpdateDto();
        dto.setWineId(UUID.randomUUID());
        dto.setCustomerID(UUID.randomUUID());
        dto.setRating(4);
        dto.setComment("Updated review");
        return dto;
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

    private ReviewDto createReviewDto(UUID id, UUID wineId, UUID customerId, int rating, String comment) {
        ReviewDto dto = new ReviewDto();
        dto.setId(id);
        dto.setWineId(wineId);
        dto.setCustomerID(customerId);
        dto.setRating(rating);
        dto.setComment(comment);
        return dto;
    }
}
