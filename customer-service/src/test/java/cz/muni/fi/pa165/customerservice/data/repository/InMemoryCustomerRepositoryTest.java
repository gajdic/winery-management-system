package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryCustomerRepositoryTest {

    private InMemoryCustomerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
    }

    @Test
    void save_customerWithoutId_assignsIdAndStoresCustomer() {
        Customer customer = createCustomer();

        Customer savedCustomer = repository.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(repository.findById(savedCustomer.getId())).contains(savedCustomer);
        assertThat(repository.findAll()).containsExactly(savedCustomer);
    }

    @Test
    void save_nullCustomer_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.save(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

    @Test
    void findById_existingId_returnsCustomer() {
        Customer customer = createCustomer();
        Customer savedCustomer = repository.save(customer);

        var foundCustomer = repository.findById(savedCustomer.getId());

        assertThat(foundCustomer).contains(savedCustomer);
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        var foundCustomer = repository.findById(UUID.randomUUID());

        assertThat(foundCustomer).isEmpty();
    }

    @Test
    void findById_nullId_returnsEmptyOptional() {
        var foundCustomer = repository.findById(null);

        assertThat(foundCustomer).isEmpty();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findAll_multipleCustomers_returnsAllCustomers() {
        Customer firstCustomer = createCustomer();
        Customer secondCustomer = new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Second Street 2", "Prague", "11000", "Czech Republic")
        );

        Customer savedFirstCustomer = repository.save(firstCustomer);
        Customer savedSecondCustomer = repository.save(secondCustomer);

        assertThat(repository.findAll())
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirstCustomer, savedSecondCustomer);
    }

    @Test
    void deleteById_existingId_removesCustomer() {
        Customer customer = createCustomer();
        Customer savedCustomer = repository.save(customer);

        repository.deleteById(savedCustomer.getId());

        assertThat(repository.findById(savedCustomer.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingId_doesNothing() {
        Customer savedCustomer = repository.save(createCustomer());

        repository.deleteById(UUID.randomUUID());

        assertThat(repository.findAll()).containsExactly(savedCustomer);
    }

    @Test
    void save_customerWithExistingId_updatesStoredCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer originalCustomer = createCustomer();
        originalCustomer.setId(customerId);
        repository.save(originalCustomer);

        Customer updatedCustomer = createCustomer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setName("Updated Customer");

        Customer savedCustomer = repository.save(updatedCustomer);

        assertThat(savedCustomer.getId()).isEqualTo(customerId);
        assertThat(repository.findAll()).containsExactly(updatedCustomer);
        assertThat(repository.findById(customerId)).contains(updatedCustomer);
    }

    @Test
    void save_customerWithExistingId_preservesOnlyLatestValue() {
        UUID customerId = UUID.randomUUID();
        Customer originalCustomer = createCustomer();
        originalCustomer.setId(customerId);
        repository.save(originalCustomer);

        Customer updatedCustomer = new Customer(
            "Updated Customer",
            "updated.customer@example.com",
            new Address("Updated Street 10", "Bratislava", "81101", "Slovakia")
        );
        updatedCustomer.setId(customerId);

        repository.save(updatedCustomer);

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(customerId)).contains(updatedCustomer);
    }

    private Customer createCustomer() {
        return new Customer(
            "John Doe",
            "john.doe@example.com",
            new Address("Main Street 1", "Brno", "60200", "Czech Republic")
        );
    }
}
