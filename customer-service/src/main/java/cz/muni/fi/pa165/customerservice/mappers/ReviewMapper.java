package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "customerID", source = "customerId")
    ReviewDto mapToDto(Review review);

    @Mapping(target = "customerId", source = "customerID")
    Review mapToEntity(ReviewCreateDto dto);

    @Mapping(target = "customerId", source = "dto.customerID")
    Review mapToEntity(UUID id, ReviewUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", source = "customerID")
    void updateEntityFromDto(ReviewUpdateDto dto, @MappingTarget Review review);
}
