package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void save_validCustomer_callsRepositoryAndReturnsSavedCustomer() {
        Customer customer = createCustomer();
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        assertThat(savedCustomer).isEqualTo(customer);
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void save_nullCustomer_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_customerWithNullName_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(customer));

        assertThat(exception.getMessage()).isEqualTo("Customer name cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_customerWithBlankName_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setName("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(customer));

        assertThat(exception.getMessage()).isEqualTo("Customer name cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_customerWithNullEmail_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setEmail(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(customer));

        assertThat(exception.getMessage()).isEqualTo("Customer email cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_customerWithBlankEmail_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setEmail(" ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(customer));

        assertThat(exception.getMessage()).isEqualTo("Customer email cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_customerWithNullAddress_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setAddress(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.save(customer));

        assertThat(exception.getMessage()).isEqualTo("Customer address cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_validCustomer_updatesExistingCustomerAndReturnsSavedCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = createCustomer();
        existingCustomer.setId(customerId);

        Customer updatedData = new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Updated Street 5", "Prague", "11000", "Czech Republic")
        );

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        Mockito.when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        Customer updatedCustomer = customerService.update(customerId, updatedData);

        assertThat(updatedCustomer.getId()).isEqualTo(customerId);
        assertThat(updatedCustomer.getName()).isEqualTo("Jane Doe");
        assertThat(updatedCustomer.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(updatedCustomer.getAddress()).isEqualTo(updatedData.getAddress());
        Mockito.verify(customerRepository).findById(customerId);
        Mockito.verify(customerRepository).save(existingCustomer);
    }

    @Test
    void update_nullCustomer_throwsIllegalArgumentException() {
        UUID customerId = UUID.randomUUID();

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(customerId, null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_nullId_throwsIllegalArgumentException() {
        Customer customer = createCustomer();

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(null, customer));

        assertThat(exception.getMessage()).isEqualTo("Customer id cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_customerWithNullName_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setName(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(UUID.randomUUID(), customer));

        assertThat(exception.getMessage()).isEqualTo("Customer name cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_customerWithBlankName_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setName(" ");

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(UUID.randomUUID(), customer));

        assertThat(exception.getMessage()).isEqualTo("Customer name cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_customerWithNullEmail_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setEmail(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(UUID.randomUUID(), customer));

        assertThat(exception.getMessage()).isEqualTo("Customer email cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_customerWithBlankEmail_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setEmail("   ");

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(UUID.randomUUID(), customer));

        assertThat(exception.getMessage()).isEqualTo("Customer email cannot be blank");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_customerWithNullAddress_throwsIllegalArgumentException() {
        Customer customer = createCustomer();
        customer.setAddress(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.update(UUID.randomUUID(), customer));

        assertThat(exception.getMessage()).isEqualTo("Customer address cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findById_existingId_returnsCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = createCustomer();
        customer.setId(customerId);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findById(customerId);

        assertThat(foundCustomer).isEqualTo(customer);
        Mockito.verify(customerRepository).findById(customerId);
    }

    @Test
    void findById_nullId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.findById(null));

        assertThat(exception.getMessage()).isEqualTo("Customer id cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void findById_nonExistingId_throwsIllegalArgumentException() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.findById(customerId));

        assertThat(exception.getMessage()).isEqualTo("Customer not found: " + customerId);
        Mockito.verify(customerRepository).findById(customerId);
    }

    @Test
    void findAll_returnsAllCustomers() {
        Customer firstCustomer = createCustomer();
        Customer secondCustomer = new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Second Street 2", "Prague", "11000", "Czech Republic")
        );
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(firstCustomer, secondCustomer));

        List<Customer> customers = customerService.findAll();

        assertThat(customers).containsExactly(firstCustomer, secondCustomer);
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        Mockito.when(customerRepository.findAll()).thenReturn(List.of());

        List<Customer> customers = customerService.findAll();

        assertThat(customers).isEmpty();
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        UUID customerId = UUID.randomUUID();
        Customer customer = createCustomer();
        customer.setId(customerId);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.deleteById(customerId);

        Mockito.verify(customerRepository).findById(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }

    @Test
    void deleteById_nullId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.deleteById(null));

        assertThat(exception.getMessage()).isEqualTo("Customer id cannot be null");
        Mockito.verify(customerRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(customerRepository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void deleteById_nonExistingId_throwsIllegalArgumentException() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> customerService.deleteById(customerId));

        assertThat(exception.getMessage()).isEqualTo("Customer not found: " + customerId);
        Mockito.verify(customerRepository).findById(customerId);
        Mockito.verify(customerRepository, Mockito.never()).deleteById(Mockito.any());
    }

    private Customer createCustomer() {
        return new Customer(
            "John Doe",
            "john.doe@example.com",
            new Address("Main Street 1", "Brno", "60200", "Czech Republic")
        );
    }
}
