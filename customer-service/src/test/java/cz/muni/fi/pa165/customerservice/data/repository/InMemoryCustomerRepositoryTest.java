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
        // Arrange
        Customer customer = createCustomer();

        // Act
        Customer savedCustomer = repository.save(customer);

        // Assert
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(repository.findById(savedCustomer.getId())).contains(savedCustomer);
        assertThat(repository.findAll()).containsExactly(savedCustomer);
    }

    @Test
    void save_nullCustomer_throwsIllegalArgumentException() {
        // Arrange + Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.save(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

    @Test
    void findById_existingId_returnsCustomer() {
        // Arrange
        Customer customer = createCustomer();
        // Act
        Customer savedCustomer = repository.save(customer);

        var foundCustomer = repository.findById(savedCustomer.getId());

        // Assert
        assertThat(foundCustomer).contains(savedCustomer);
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        // Arrange + Act
        var foundCustomer = repository.findById(UUID.randomUUID());

        // Assert
        assertThat(foundCustomer).isEmpty();
    }

    @Test
    void findById_nullId_returnsEmptyOptional() {
        // Arrange + Act
        var foundCustomer = repository.findById(null);

        // Assert
        assertThat(foundCustomer).isEmpty();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // Arrange + Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findAll_multipleCustomers_returnsAllCustomers() {
        // Arrange
        Customer firstCustomer = createCustomer();
        Customer secondCustomer = new Customer(
            "Jane Doe",
            "jane.doe@example.com",
            new Address("Second Street 2", "Prague", "11000", "Czech Republic")
        );

        // Act
        Customer savedFirstCustomer = repository.save(firstCustomer);
        Customer savedSecondCustomer = repository.save(secondCustomer);

        // Assert
        assertThat(repository.findAll())
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirstCustomer, savedSecondCustomer);
    }

    @Test
    void deleteById_existingId_removesCustomer() {
        // Arrange
        Customer customer = createCustomer();
        // Act
        Customer savedCustomer = repository.save(customer);

        repository.deleteById(savedCustomer.getId());

        // Assert
        assertThat(repository.findById(savedCustomer.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingId_doesNothing() {
        // Arrange + Act
        Customer savedCustomer = repository.save(createCustomer());

        repository.deleteById(UUID.randomUUID());

        // Assert
        assertThat(repository.findAll()).containsExactly(savedCustomer);
    }

    @Test
    void save_customerWithExistingId_updatesStoredCustomer() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Customer originalCustomer = createCustomer();
        originalCustomer.setId(customerId);
        // Act
        repository.save(originalCustomer);

        Customer updatedCustomer = createCustomer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setName("Updated Customer");

        // Act
        Customer savedCustomer = repository.save(updatedCustomer);

        // Assert
        assertThat(savedCustomer.getId()).isEqualTo(customerId);
        assertThat(repository.findAll()).containsExactly(updatedCustomer);
        assertThat(repository.findById(customerId)).contains(updatedCustomer);
    }

    @Test
    void save_customerWithExistingId_preservesOnlyLatestValue() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Customer originalCustomer = createCustomer();
        originalCustomer.setId(customerId);
        // Act
        repository.save(originalCustomer);

        Customer updatedCustomer = new Customer(
            "Updated Customer",
            "updated.customer@example.com",
            new Address("Updated Street 10", "Bratislava", "81101", "Slovakia")
        );
        updatedCustomer.setId(customerId);

        // Act
        repository.save(updatedCustomer);

        // Assert
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
