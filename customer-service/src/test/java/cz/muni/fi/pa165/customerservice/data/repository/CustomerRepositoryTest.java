package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save_ValidCustomer_PersistsCustomer() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();

        // Act
        Customer saved = customerRepository.saveAndFlush(customer);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void findById_ExistingCustomer_ReturnsCustomer() {
        // Arrange
        Customer customer = customerRepository.saveAndFlush(TestDataFactory.customerEntity());

        // Act
        Optional<Customer> found = customerRepository.findById(customer.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(customer.getName());
    }

    @Test
    void findAll_MultipleCustomers_ReturnsAllCustomers() {
        // Arrange
        customerRepository.saveAndFlush(TestDataFactory.customerEntity());

        Customer second = TestDataFactory.customerEntity();
        second.setEmail("different@email.com");
        customerRepository.save(second);

        // Act
        List<Customer> customers = customerRepository.findAll();

        // Assert
        assertThat(customers).hasSize(2);
    }

    @Test
    void deleteById_ExistingCustomer_RemovesCustomer() {
        // Arrange
        Customer customer = customerRepository.saveAndFlush(TestDataFactory.customerEntity());

        // Act
        customerRepository.deleteById(customer.getId());

        // Assert
        Optional<Customer> found = customerRepository.findById(customer.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void save_NullName_ThrowsException() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customer.setName(null);

        // Act & Assert
        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullEmail_ThrowsException() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customer.setEmail(null);

        // Act & Assert
        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullAddress_ThrowsException() {
        // Arrange
        Customer customer = TestDataFactory.customerEntity();
        customer.setAddress(null);

        // Act & Assert
        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
