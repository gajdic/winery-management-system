package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.AddressDto;
import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto mapToDto(Customer customer);

    Customer mapToEntity(CustomerCreateDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CustomerUpdateDto dto, @MappingTarget Customer customer);

    AddressDto mapToDto(Address address);

    Address mapToEntity(AddressDto dto);
}
