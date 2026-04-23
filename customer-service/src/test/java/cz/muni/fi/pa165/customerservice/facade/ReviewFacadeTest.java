package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.mappers.ReviewMapper;
import cz.muni.fi.pa165.customerservice.service.ReviewService;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
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
        // Arrange
        ReviewCreateDto createDto = createReviewCreateDto();
        Review review = createReview();
        Review savedReview = createReview();
        savedReview.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(savedReview.getId(), savedReview.getWineId(), savedReview.getCustomer().getId(), savedReview.getRating(), savedReview.getComment());

        Mockito.when(reviewMapper.mapToEntity(createDto)).thenReturn(review);
        Mockito.when(reviewService.save(review)).thenReturn(savedReview);
        Mockito.when(reviewMapper.mapToDto(savedReview)).thenReturn(reviewDto);

        // Act
        ReviewDto createdReview = reviewFacade.createReview(createDto);

        // Assert
        assertThat(createdReview).isEqualTo(reviewDto);
        Mockito.verify(reviewMapper).mapToEntity(createDto);
        Mockito.verify(reviewService).save(review);
        Mockito.verify(reviewMapper).mapToDto(savedReview);
    }

    @Test
    void getReview_existingId_returnsMappedDto() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Review review = createReview();
        review.setId(reviewId);
        ReviewDto reviewDto = createReviewDto(reviewId, review.getWineId(), review.getCustomer().getId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findById(reviewId)).thenReturn(review);
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        // Act
        ReviewDto foundReview = reviewFacade.getReview(reviewId);

        // Assert
        assertThat(foundReview).isEqualTo(reviewDto);
        Mockito.verify(reviewService).findById(reviewId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getAllReviews_returnsMappedDtos() {
        // Arrange
        Review firstReview = createReview();
        firstReview.setId(UUID.randomUUID());
        Review secondReview = createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 3, "Average");
        secondReview.setId(UUID.randomUUID());

        ReviewDto firstDto = createReviewDto(firstReview.getId(), firstReview.getWineId(), firstReview.getCustomer().getId(), firstReview.getRating(), firstReview.getComment());
        ReviewDto secondDto = createReviewDto(secondReview.getId(), secondReview.getWineId(), secondReview.getCustomer().getId(), secondReview.getRating(), secondReview.getComment());

        Mockito.when(reviewService.findAll()).thenReturn(List.of(firstReview, secondReview));
        Mockito.when(reviewMapper.mapToDto(firstReview)).thenReturn(firstDto);
        Mockito.when(reviewMapper.mapToDto(secondReview)).thenReturn(secondDto);

        // Act
        List<ReviewDto> reviews = reviewFacade.getAllReviews();

        // Assert
        assertThat(reviews).containsExactly(firstDto, secondDto);
        Mockito.verify(reviewService).findAll();
        Mockito.verify(reviewMapper).mapToDto(firstReview);
        Mockito.verify(reviewMapper).mapToDto(secondReview);
    }

    @Test
    void getAllReviews_emptyList_returnsEmptyListWithoutMapping() {
        // Arrange
        Mockito.when(reviewService.findAll()).thenReturn(List.of());

        // Act
        List<ReviewDto> reviews = reviewFacade.getAllReviews();

        // Assert
        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findAll();
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void getReviewsByCustomer_existingCustomerId_returnsMappedDtos() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Review review = createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 5, "Excellent");
        review.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(review.getId(), review.getWineId(), review.getCustomer().getId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findByCustomerId(customerId)).thenReturn(List.of(review));
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        // Act
        List<ReviewDto> reviews = reviewFacade.getReviewsByCustomer(customerId);

        // Assert
        assertThat(reviews).containsExactly(reviewDto);
        Mockito.verify(reviewService).findByCustomerId(customerId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getReviewsByCustomer_nonExistingCustomerId_returnsEmptyListWithoutMapping() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Mockito.when(reviewService.findByCustomerId(customerId)).thenReturn(List.of());

        // Act
        List<ReviewDto> reviews = reviewFacade.getReviewsByCustomer(customerId);

        // Assert
        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findByCustomerId(customerId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void getReviewsByWine_existingWineId_returnsMappedDtos() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        Review review = createReview(TestDataFactory.customerEntity(), wineId, 4, "Very good");
        review.setId(UUID.randomUUID());
        ReviewDto reviewDto = createReviewDto(review.getId(), review.getWineId(), review.getCustomer().getId(), review.getRating(), review.getComment());

        Mockito.when(reviewService.findByWineId(wineId)).thenReturn(List.of(review));
        Mockito.when(reviewMapper.mapToDto(review)).thenReturn(reviewDto);

        // Act
        List<ReviewDto> reviews = reviewFacade.getReviewsByWine(wineId);

        // Assert
        assertThat(reviews).containsExactly(reviewDto);
        Mockito.verify(reviewService).findByWineId(wineId);
        Mockito.verify(reviewMapper).mapToDto(review);
    }

    @Test
    void getReviewsByWine_nonExistingWineId_returnsEmptyListWithoutMapping() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        Mockito.when(reviewService.findByWineId(wineId)).thenReturn(List.of());

        // Act
        List<ReviewDto> reviews = reviewFacade.getReviewsByWine(wineId);

        // Assert
        assertThat(reviews).isEmpty();
        Mockito.verify(reviewService).findByWineId(wineId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void updateReview_validDto_mapsUpdatesAndReturnsDto() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        ReviewUpdateDto updateDto = createReviewUpdateDto();
        Review mappedReview = createReview(TestDataFactory.customerEntity(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());
        mappedReview.setId(reviewId);
        Review updatedReview = createReview(TestDataFactory.customerEntity(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());
        updatedReview.setId(reviewId);
        ReviewDto reviewDto = createReviewDto(reviewId, updatedReview.getWineId(), updatedReview.getCustomer().getId(), updatedReview.getRating(), updatedReview.getComment());

        Mockito.when(reviewMapper.mapToEntity(reviewId, updateDto)).thenReturn(mappedReview);
        Mockito.when(reviewService.update(reviewId, mappedReview)).thenReturn(updatedReview);
        Mockito.when(reviewMapper.mapToDto(updatedReview)).thenReturn(reviewDto);

        // Act
        ReviewDto result = reviewFacade.updateReview(reviewId, updateDto);

        // Assert
        assertThat(result).isEqualTo(reviewDto);
        Mockito.verify(reviewMapper).mapToEntity(reviewId, updateDto);
        Mockito.verify(reviewService).update(reviewId, mappedReview);
        Mockito.verify(reviewMapper).mapToDto(updatedReview);
    }

    @Test
    void createReview_serviceThrowsException_propagatesException() {
        // Arrange
        ReviewCreateDto createDto = createReviewCreateDto();
        Review review = createReview();

        Mockito.when(reviewMapper.mapToEntity(createDto)).thenReturn(review);
        Mockito.when(reviewService.save(review)).thenThrow(new IllegalArgumentException("Review cannot be null"));

        // Act + Assert
        assertThatThrownBy(() -> reviewFacade.createReview(createDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Review cannot be null");

        // Assert
        Mockito.verify(reviewMapper).mapToEntity(createDto);
        Mockito.verify(reviewService).save(review);
        Mockito.verify(reviewMapper, Mockito.never()).mapToDto(Mockito.any());
    }

    @Test
    void getReview_serviceThrowsException_propagatesException() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Mockito.when(reviewService.findById(reviewId)).thenThrow(new ResourceNotFoundException("Review not found: " + reviewId));

        // Act + Assert
        assertThatThrownBy(() -> reviewFacade.getReview(reviewId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Review not found: " + reviewId);

        // Assert
        Mockito.verify(reviewService).findById(reviewId);
        Mockito.verifyNoInteractions(reviewMapper);
    }

    @Test
    void updateReview_serviceThrowsException_propagatesException() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        ReviewUpdateDto updateDto = createReviewUpdateDto();
        Review mappedReview = createReview(TestDataFactory.customerEntity(), updateDto.getWineId(), updateDto.getRating(), updateDto.getComment());

        Mockito.when(reviewMapper.mapToEntity(reviewId, updateDto)).thenReturn(mappedReview);
        Mockito.when(reviewService.update(reviewId, mappedReview))
            .thenThrow(new ResourceNotFoundException("Review not found: " + reviewId));

        // Act + Assert
        assertThatThrownBy(() -> reviewFacade.updateReview(reviewId, updateDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Review not found: " + reviewId);

        // Assert
        Mockito.verify(reviewMapper).mapToEntity(reviewId, updateDto);
        Mockito.verify(reviewService).update(reviewId, mappedReview);
        Mockito.verify(reviewMapper, Mockito.never()).mapToDto(Mockito.any());
    }

    @Test
    void deleteReview_callsServiceDeleteById() {
        // Arrange
        UUID reviewId = UUID.randomUUID();

        // Act
        reviewFacade.deleteReview(reviewId);

        // Assert
        Mockito.verify(reviewService).deleteById(reviewId);
    }

    @Test
    void deleteReview_serviceThrowsException_propagatesException() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        Mockito.doThrow(new ResourceNotFoundException("Review not found: " + reviewId))
            .when(reviewService).deleteById(reviewId);

        // Act + Assert
        assertThatThrownBy(() -> reviewFacade.deleteReview(reviewId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Review not found: " + reviewId);

        // Assert
        Mockito.verify(reviewService).deleteById(reviewId);
    }

    private ReviewCreateDto createReviewCreateDto() {
        ReviewCreateDto dto = new ReviewCreateDto();
        dto.setWineId(UUID.randomUUID());
        dto.setCustomerId(UUID.randomUUID());
        dto.setRating(5);
        dto.setComment("Great wine");
        return dto;
    }

    private ReviewUpdateDto createReviewUpdateDto() {
        ReviewUpdateDto dto = new ReviewUpdateDto();
        dto.setWineId(UUID.randomUUID());
        dto.setCustomerId(UUID.randomUUID());
        dto.setRating(4);
        dto.setComment("Updated review");
        return dto;
    }

    private Review createReview() {
        return createReview(TestDataFactory.customerEntity(), UUID.randomUUID(), 5, "Great wine");
    }

    private Review createReview(Customer customer, UUID wineId, int rating, String comment) {
        Review review = new Review();
        review.setCustomer(TestDataFactory.customerEntity());
        review.setWineId(wineId);
        review.setRating(rating);
        review.setComment(comment);
        return review;
    }

    private ReviewDto createReviewDto(UUID id, UUID wineId, UUID customerId, int rating, String comment) {
        ReviewDto dto = new ReviewDto();
        dto.setId(id);
        dto.setWineId(wineId);
        dto.setCustomerId(customerId);
        dto.setRating(rating);
        dto.setComment(comment);
        return dto;
    }
}
