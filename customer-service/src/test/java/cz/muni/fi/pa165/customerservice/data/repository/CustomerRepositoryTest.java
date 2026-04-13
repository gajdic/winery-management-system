package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save_validCustomer_customerIsPersisted() {
        Customer customer = createCustomer();

        Customer saved = customerRepository.save(customer);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void findById_existingCustomer_customerIsReturned() {
        Customer customer = customerRepository.save(createCustomer());

        Optional<Customer> found = customerRepository.findById(customer.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(customer.getName());
    }

    @Test
    void findAll_multipleCustomers_returnsAllCustomers() {
        customerRepository.save(createCustomer());
        customerRepository.save(createCustomer());

        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(2);
    }

    @Test
    void deleteById_existingCustomer_customerIsRemoved() {
        Customer customer = customerRepository.save(createCustomer());

        customerRepository.deleteById(customer.getId());

        Optional<Customer> found = customerRepository.findById(customer.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void save_nullName_throwsException() {
        Customer customer = createCustomer();
        customer.setName(null);

        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_nullEmail_throwsException() {
        Customer customer = createCustomer();
        customer.setEmail(null);

        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_nullAddress_throwsException() {
        Customer customer = createCustomer();
        customer.setAddress(null);

        assertThatThrownBy(() -> customerRepository.saveAndFlush(customer))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john_" + UUID.randomUUID() + "@example.com");

        Address address = new Address();
        address.setCity("Brno");
        address.setStreet("Main Street 1");
        address.setPostCode("60200");
        address.setCountry("CZ");

        customer.setAddress(address);

        return customer;
    }
}
