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

    @Mapping(target = "customerId", source = "customerId")
    ReviewDto mapToDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", source = "customerId")
    Review mapToEntity(ReviewCreateDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "dto.customerId")
    Review mapToEntity(UUID id, ReviewUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", source = "customerId")
    void updateEntityFromDto(ReviewUpdateDto dto, @MappingTarget Review review);
}
