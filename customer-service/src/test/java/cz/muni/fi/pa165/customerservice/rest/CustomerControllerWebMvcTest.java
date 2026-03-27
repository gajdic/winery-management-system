package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.AddressDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.facade.CustomerFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerFacade customerFacade;

    @Test
    void createCustomer_validRequest_returnsCreatedCustomer() throws Exception {
        CustomerDto customerDto = createCustomerDto(UUID.randomUUID(), "John Doe", "john.doe@example.com");
        when(customerFacade.createCustomer(any())).thenReturn(customerDto);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateCustomerRequestJson()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(customerDto.getId().toString()))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.address.street").value("Main Street 1"));
    }

    @Test
    void createCustomer_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCreateCustomerRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomer_existingId_returnsCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = createCustomerDto(customerId, "John Doe", "john.doe@example.com");
        when(customerFacade.getCustomer(customerId)).thenReturn(customerDto);

        mockMvc.perform(get("/customers/{id}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(customerId.toString()))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getCustomer_nonExistingId_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();
        when(customerFacade.getCustomer(customerId)).thenThrow(new IllegalArgumentException("Customer not found: " + customerId));

        mockMvc.perform(get("/customers/{id}", customerId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void getAllCustomers_returnsAllCustomers() throws Exception {
        CustomerDto firstCustomer = createCustomerDto(UUID.randomUUID(), "John Doe", "john.doe@example.com");
        CustomerDto secondCustomer = createCustomerDto(UUID.randomUUID(), "Jane Doe", "jane.doe@example.com");
        when(customerFacade.getAllCustomers()).thenReturn(List.of(firstCustomer, secondCustomer));

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(firstCustomer.getId().toString()))
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].id").value(secondCustomer.getId().toString()))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void updateCustomer_validRequest_returnsUpdatedCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDto updatedCustomer = createCustomerDto(customerId, "Jane Doe", "jane.doe@example.com");
        when(customerFacade.updateCustomer(eq(customerId), any())).thenReturn(updatedCustomer);

        mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateCustomerRequestJson()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(customerId.toString()))
            .andExpect(jsonPath("$.name").value("Jane Doe"))
            .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    @Test
    void updateCustomer_nonExistingId_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();
        when(customerFacade.updateCustomer(eq(customerId), any()))
            .thenThrow(new IllegalArgumentException("Customer not found: " + customerId));

        mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateCustomerRequestJson()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void updateCustomer_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/customers/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCreateCustomerRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_existingId_returnsNoContent() throws Exception {
        UUID customerId = UUID.randomUUID();
        doNothing().when(customerFacade).deleteCustomer(customerId);

        mockMvc.perform(delete("/customers/{id}", customerId))
            .andExpect(status().isNoContent());

        verify(customerFacade).deleteCustomer(customerId);
    }

    @Test
    void deleteCustomer_nonExistingId_returnsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();
        doThrow(new IllegalArgumentException("Customer not found: " + customerId))
            .when(customerFacade).deleteCustomer(customerId);

        mockMvc.perform(delete("/customers/{id}", customerId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Customer not found: " + customerId));
    }

    @Test
    void getCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/customers/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/customers/not-a-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateCustomerRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/customers/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomers_emptyList_returnsEmptyArray() throws Exception {
        when(customerFacade.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getCustomer_unexpectedError_returnsInternalServerError() throws Exception {
        UUID customerId = UUID.randomUUID();
        when(customerFacade.getCustomer(customerId)).thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/customers/{id}", customerId))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value("500 INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("Unexpected failure"));
    }

    private CustomerDto createCustomerDto(UUID id, String name, String email) {
        CustomerDto dto = new CustomerDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setAddress(new AddressDto("Main Street 1", "Brno", "60200", "Czech Republic"));
        return dto;
    }

    private String validCreateCustomerRequestJson() {
        return """
            {
              "name": "John Doe",
              "email": "john.doe@example.com",
              "address": {
                "street": "Main Street 1",
                "city": "Brno",
                "postCode": "60200",
                "country": "Czech Republic"
              }
            }
            """;
    }

    private String invalidCreateCustomerRequestJson() {
        return """
            {
              "name": "",
              "email": "",
              "address": null
            }
            """;
    }

    private String validUpdateCustomerRequestJson() {
        return """
            {
              "name": "Jane Doe",
              "email": "jane.doe@example.com",
              "address": {
                "street": "Updated Street 5",
                "city": "Prague",
                "postCode": "11000",
                "country": "Czech Republic"
              }
            }
            """;
    }
}
