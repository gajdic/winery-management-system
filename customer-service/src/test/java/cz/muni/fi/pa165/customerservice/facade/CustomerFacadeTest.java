package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.AddressDto;
import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.mappers.CustomerMapper;
import cz.muni.fi.pa165.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerFacade customerFacade;

    @Test
    void createCustomer_validDto_mapsSavesAndReturnsDto() {
        CustomerCreateDto createDto = createCustomerCreateDto();
        Customer customer = createCustomer();
        Customer savedCustomer = createCustomer();
        savedCustomer.setId(UUID.randomUUID());
        CustomerDto customerDto = createCustomerDto(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getEmail());

        Mockito.when(customerMapper.mapToEntity(createDto)).thenReturn(customer);
        Mockito.when(customerService.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.mapToDto(savedCustomer)).thenReturn(customerDto);

        CustomerDto createdCustomer = customerFacade.createCustomer(createDto);

        assertThat(createdCustomer).isEqualTo(customerDto);
        Mockito.verify(customerMapper).mapToEntity(createDto);
        Mockito.verify(customerService).save(customer);
        Mockito.verify(customerMapper).mapToDto(savedCustomer);
    }

    @Test
    void getCustomer_existingId_returnsMappedDto() {
        UUID customerId = UUID.randomUUID();
        Customer customer = createCustomer();
        customer.setId(customerId);
        CustomerDto customerDto = createCustomerDto(customerId, customer.getName(), customer.getEmail());

        Mockito.when(customerService.findById(customerId)).thenReturn(customer);
        Mockito.when(customerMapper.mapToDto(customer)).thenReturn(customerDto);

        CustomerDto foundCustomer = customerFacade.getCustomer(customerId);

        assertThat(foundCustomer).isEqualTo(customerDto);
        Mockito.verify(customerService).findById(customerId);
        Mockito.verify(customerMapper).mapToDto(customer);
    }

    @Test
    void getAllCustomers_returnsMappedDtos() {
        Customer firstCustomer = createCustomer();
        firstCustomer.setId(UUID.randomUUID());
        Customer secondCustomer = new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Second Street 2", "Prague", "11000", "Czech Republic")
        );
        secondCustomer.setId(UUID.randomUUID());

        CustomerDto firstDto = createCustomerDto(firstCustomer.getId(), firstCustomer.getName(), firstCustomer.getEmail());
        CustomerDto secondDto = createCustomerDto(secondCustomer.getId(), secondCustomer.getName(), secondCustomer.getEmail());

        Mockito.when(customerService.findAll()).thenReturn(List.of(firstCustomer, secondCustomer));
        Mockito.when(customerMapper.mapToDto(firstCustomer)).thenReturn(firstDto);
        Mockito.when(customerMapper.mapToDto(secondCustomer)).thenReturn(secondDto);

        List<CustomerDto> customers = customerFacade.getAllCustomers();

        assertThat(customers).containsExactly(firstDto, secondDto);
        Mockito.verify(customerService).findAll();
        Mockito.verify(customerMapper).mapToDto(firstCustomer);
        Mockito.verify(customerMapper).mapToDto(secondCustomer);
    }

    @Test
    void updateCustomer_validDto_mapsUpdatesAndReturnsDto() {
        UUID customerId = UUID.randomUUID();
        CustomerUpdateDto updateDto = createCustomerUpdateDto();
        Customer mappedCustomer = createUpdatedCustomer();
        mappedCustomer.setId(customerId);
        Customer updatedCustomer = createUpdatedCustomer();
        updatedCustomer.setId(customerId);
        CustomerDto customerDto = createCustomerDto(customerId, updatedCustomer.getName(), updatedCustomer.getEmail());

        Mockito.when(customerMapper.mapToEntity(customerId, updateDto)).thenReturn(mappedCustomer);
        Mockito.when(customerService.update(customerId, mappedCustomer)).thenReturn(updatedCustomer);
        Mockito.when(customerMapper.mapToDto(updatedCustomer)).thenReturn(customerDto);

        CustomerDto result = customerFacade.updateCustomer(customerId, updateDto);

        assertThat(result).isEqualTo(customerDto);
        Mockito.verify(customerMapper).mapToEntity(customerId, updateDto);
        Mockito.verify(customerService).update(customerId, mappedCustomer);
        Mockito.verify(customerMapper).mapToDto(updatedCustomer);
    }

    @Test
    void deleteCustomer_callsServiceDeleteById() {
        UUID customerId = UUID.randomUUID();

        customerFacade.deleteCustomer(customerId);

        Mockito.verify(customerService).deleteById(customerId);
    }

    private CustomerCreateDto createCustomerCreateDto() {
        return new CustomerCreateDto(
            "John Doe",
            "john.doe@example.com",
            new AddressDto("Main Street 1", "Brno", "60200", "Czech Republic")
        );
    }

    private CustomerUpdateDto createCustomerUpdateDto() {
        return new CustomerUpdateDto(
            "Jane Doe",
            "jane.doe@example.com",
            new AddressDto("Updated Street 5", "Prague", "11000", "Czech Republic")
        );
    }

    private Customer createCustomer() {
        return new Customer(
            "John Doe",
            "john.doe@example.com",
            new Address("Main Street 1", "Brno", "60200", "Czech Republic")
        );
    }

    private Customer createUpdatedCustomer() {
        return new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Updated Street 5", "Prague", "11000", "Czech Republic")
        );
    }

    private CustomerDto createCustomerDto(UUID id, String name, String email) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setName(name);
        customerDto.setEmail(email);
        customerDto.setAddress(new AddressDto("Main Street 1", "Brno", "60200", "Czech Republic"));
        return customerDto;
    }
}
