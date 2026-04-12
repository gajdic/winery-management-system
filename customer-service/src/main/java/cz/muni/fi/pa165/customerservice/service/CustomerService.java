package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be blank");
        }

        if (customer.getAddress() == null) {
            throw new IllegalArgumentException("Customer address cannot be null");
        }

        return customerRepository.save(customer);
    }

    public Customer update(UUID id, Customer updatedData) {
        if (updatedData == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (updatedData.getName() == null || updatedData.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }

        if (updatedData.getEmail() == null || updatedData.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be blank");
        }

        if (updatedData.getAddress() == null) {
            throw new IllegalArgumentException("Customer address cannot be null");
        }

        Customer existing = findById(id);

        existing.setName(updatedData.getName());
        existing.setEmail(updatedData.getEmail());
        existing.setAddress(updatedData.getAddress());

        return customerRepository.save(existing);
    }

    public Customer findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer id cannot be null");
        }

        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer id cannot be null");
        }

        findById(id);
        customerRepository.deleteById(id);
    }
}
