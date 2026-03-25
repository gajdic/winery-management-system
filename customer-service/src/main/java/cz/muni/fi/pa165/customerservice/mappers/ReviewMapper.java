package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.ReviewCreateDto;
import cz.muni.fi.pa165.customerservice.api.ReviewDto;
import cz.muni.fi.pa165.customerservice.api.ReviewUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDto mapToDto(Review review);

    Review mapToEntity(ReviewCreateDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ReviewUpdateDto dto,@MappingTarget Review review);
}
