package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.AddressDto;
import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto mapToDto(Address address);

    Address mapToEntity(AddressDto dto);
}
