package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void getCustomer_existingCustomer_returnsCustomerDto() throws Exception {
        Customer customer = new Customer(
            "Integration User",
            "integration." + UUID.randomUUID() + "@example.com",
            new Address("Integration Street 1", "Brno", "60200", "Czech Republic")
        );
        customer = customerRepository.save(customer);

        mockMvc.perform(get("/customers/{id}", customer.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(customer.getId().toString()))
            .andExpect(jsonPath("$.name").value("Integration User"))
            .andExpect(jsonPath("$.email").value(customer.getEmail()))
            .andExpect(jsonPath("$.address.street").value("Integration Street 1"));
    }

    @Test
    void getCustomer_nonExistingCustomer_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(get("/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void getAllCustomers_returnsCustomersIncludingInsertedCustomer() throws Exception {
        String email = "all-customers." + UUID.randomUUID() + "@example.com";
        Customer customer = new Customer(
            "All Customers User",
            email,
            new Address("Collection Street 7", "Brno", "60200", "Czech Republic")
        );
        customerRepository.save(customer);

        mockMvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].email", hasItem(email)))
            .andExpect(jsonPath("$[*].name", hasItem("All Customers User")));
    }

    @Test
    void createCustomer_validRequest_returnsCreatedCustomerDto() throws Exception {
        String email = "created." + UUID.randomUUID() + "@example.com";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Created User",
                      "email": "%s",
                      "address": {
                        "street": "Created Street 9",
                        "city": "Prague",
                        "postCode": "11000",
                        "country": "Czech Republic"
                      }
                    }
                    """.formatted(email)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Created User"))
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.address.street").value("Created Street 9"));
    }

    @Test
    void createCustomer_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "",
                      "email": "",
                      "address": null
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomer_existingCustomer_returnsUpdatedCustomerDto() throws Exception {
        Customer customer = new Customer(
            "Before Update",
            "before-update." + UUID.randomUUID() + "@example.com",
            new Address("Before Street 1", "Brno", "60200", "Czech Republic")
        );
        customer = customerRepository.save(customer);

        mockMvc.perform(put("/customers/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "After Update",
                      "email": "after.update@example.com",
                      "address": {
                        "street": "After Street 10",
                        "city": "Prague",
                        "postCode": "11000",
                        "country": "Czech Republic"
                      }
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(customer.getId().toString()))
            .andExpect(jsonPath("$.name").value("After Update"))
            .andExpect(jsonPath("$.email").value("after.update@example.com"))
            .andExpect(jsonPath("$.address.street").value("After Street 10"));
    }

    @Test
    void updateCustomer_nonExistingCustomer_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "After Update",
                      "email": "after.update@example.com",
                      "address": {
                        "street": "After Street 10",
                        "city": "Prague",
                        "postCode": "11000",
                        "country": "Czech Republic"
                      }
                    }
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void updateCustomer_invalidRequest_returnsBadRequest() throws Exception {
        Customer customer = new Customer(
            "Before Invalid Update",
            "before-invalid-update." + UUID.randomUUID() + "@example.com",
            new Address("Before Street 2", "Brno", "60200", "Czech Republic")
        );
        customer = customerRepository.save(customer);

        mockMvc.perform(put("/customers/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "",
                      "email": "",
                      "address": null
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_existingCustomer_returnsNoContentAndRemovesCustomer() throws Exception {
        Customer customer = new Customer(
            "Delete User",
            "delete." + UUID.randomUUID() + "@example.com",
            new Address("Delete Street 4", "Brno", "60200", "Czech Republic")
        );
        customer = customerRepository.save(customer);

        mockMvc.perform(delete("/customers/{id}", customer.getId()))
            .andExpect(status().isNoContent());

        org.assertj.core.api.Assertions.assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void deleteCustomer_nonExistingCustomer_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(delete("/customers/{id}", customerId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void getCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/customers/not-a-uuid")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/customers/not-a-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "After Update",
                      "email": "after.update@example.com",
                      "address": {
                        "street": "After Street 10",
                        "city": "Prague",
                        "postCode": "11000",
                        "country": "Czech Republic"
                      }
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/customers/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomers_emptyRepository_returnsEmptyList() throws Exception {
        for (Customer customer : customerRepository.findAll()) {
            customerRepository.deleteById(customer.getId());
        }

        mockMvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }
}
