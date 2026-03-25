package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.mappers.CustomerMapper;
import cz.muni.fi.pa165.customerservice.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerFacade {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerFacade(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    public CustomerDto createCustomer(CustomerCreateDto dto) {
        Customer customer = customerMapper.mapToEntity(dto);
        Customer saved = customerService.save(customer);
        return customerMapper.mapToDto(saved);
    }

    public CustomerDto getCustomer(UUID id) {
        Customer customer = customerService.findById(id);
        return customerMapper.mapToDto(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerService.findAll()
            .stream()
            .map(customerMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public CustomerDto updateCustomer(UUID id, CustomerUpdateDto dto) {
        Customer customer = customerService.findById(id);
        customerMapper.updateEntityFromDto(dto, customer);
        Customer updated = customerService.save(customer);
        return customerMapper.mapToDto(updated);
    }

    public void deleteCustomer(UUID id) {
        customerService.deleteById(id);
    }
}
