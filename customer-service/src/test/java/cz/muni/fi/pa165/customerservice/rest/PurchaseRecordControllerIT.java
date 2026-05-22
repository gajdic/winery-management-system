package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import cz.muni.fi.pa165.customerservice.data.repository.PurchaseRecordRepository;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseRecordControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRecordRepository purchaseRecordRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void getPurchase_existingPurchase_returnsPurchaseDto() throws Exception {

        // Arrange
        Customer customer = customerRepository.saveAndFlush(
            TestDataFactory.customerEntity()
        );

        PurchaseRecord purchase = createPurchaseRecord(
            customer,
            UUID.randomUUID(),
            3,
            12.5,
            LocalDateTime.of(2026, 3, 1, 10, 15)
        );

        purchase = purchaseRecordRepository.saveAndFlush(purchase);

        // Act + Assert
        mockMvc.perform(get("/purchases/{id}", purchase.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(purchase.getId().toString()))
            .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
            .andExpect(jsonPath("$.wineId").value(purchase.getWineId().toString()))
            .andExpect(jsonPath("$.quantity").value(3))
            .andExpect(jsonPath("$.pricePerBottle").value(12.5))
            .andExpect(jsonPath("$.purchaseDate").value("2026-03-01T10:15:00"));
    }

    @Test
    void getPurchase_nonExistingPurchase_returnsNotFound() throws Exception {
        // Arrange
        UUID purchaseId = UUID.randomUUID();

        // Act + Assert
        mockMvc.perform(get("/purchases/{id}", purchaseId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("PurchaseRecord not found: " + purchaseId));
    }

    @Test
    void getAllPurchases_returnsPurchasesIncludingInsertedPurchase() throws Exception {

        // Arrange
        Customer customer = customerRepository.saveAndFlush(
            TestDataFactory.customerEntity()
        );

        PurchaseRecord purchase = createPurchaseRecord(
            customer,
            UUID.randomUUID(),
            2,
            9.99,
            LocalDateTime.of(2026, 3, 2, 8, 0)
        );

        purchaseRecordRepository.saveAndFlush(purchase);

        // Act + Assert
        mockMvc.perform(get("/purchases")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].quantity", hasItem(2)));
    }

    @Test
    void createPurchase_validRequest_returnsCreatedPurchaseDto() throws Exception {

        Customer customer = customerRepository.save(TestDataFactory.customerEntity());
        UUID wineId = UUID.randomUUID();

        mockMvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreatePurchaseJson(customer.getId(), wineId, 4, 15.75)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.quantity").value(4))
            .andExpect(jsonPath("$.pricePerBottle").value(15.75))
            .andExpect(jsonPath("$.purchaseDate").isNotEmpty());
    }

    @Test
    void createPurchase_invalidRequest_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "customerId": null,
                      "wineId": null,
                      "quantity": 0,
                      "pricePerBottle": -1
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByCustomerId_existingCustomer_returnsMatchingPurchases() throws Exception {

        // Arrange
        Customer customer = customerRepository.save(
            TestDataFactory.customerEntity()
        );

        UUID customerId = customer.getId();

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 1, 10.0,
                LocalDateTime.of(2026, 3, 3, 9, 0))
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 5, 6.5,
                LocalDateTime.of(2026, 3, 3, 10, 0))
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/customer/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].customerId").value(customerId.toString()));
    }

    @Test
    void getByCustomerId_nonExistingCustomer_returnsEmptyArray() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/customer/{customerId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByWineId_existingWine_returnsMatchingPurchases() throws Exception {

        // Arrange
        Customer customer = customerRepository.save(
            TestDataFactory.customerEntity()
        );

        UUID wineId = UUID.randomUUID();

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, wineId, 2, 18.0,
                LocalDateTime.of(2026, 3, 4, 9, 0))
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/wine/{wineId}", wineId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].wineId").value(wineId.toString()));
    }

    @Test
    void getByWineId_nonExistingWine_returnsEmptyArray() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/wine/{wineId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByQuantity_returnsPurchasesWithinRange() throws Exception {

        // Arrange
        Customer customer = customerRepository.save(
            TestDataFactory.customerEntity()
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 1, 5.0,
                LocalDateTime.of(2026, 3, 5, 9, 0))
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 4, 8.0,
                LocalDateTime.of(2026, 3, 5, 10, 0))
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 7, 11.0,
                LocalDateTime.of(2026, 3, 5, 11, 0))
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "2")
                .param("max", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].quantity", hasItem(4)));
    }

    @Test
    void getByQuantity_onlyMinProvided_usesDefaultMax() throws Exception {

        // Arrange
        Customer customer = customerRepository.save(
            TestDataFactory.customerEntity()
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(customer, UUID.randomUUID(), 6, 8.0,
                LocalDateTime.of(2026, 3, 5, 12, 0))
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].quantity", hasItem(6)));
    }

    @Test
    void getByQuantity_onlyMaxProvided_usesDefaultMin() throws Exception {
        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        purchaseRecordRepository.save(
            createPurchaseRecord(
                customer,
                UUID.randomUUID(),
                1,
                8.0,
                LocalDateTime.of(2026, 3, 5, 13, 0)
            )
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("max", "2")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].quantity", hasItem(1)));
    }

    @Test
    void getByQuantity_nonExistingRange_returnsEmptyArray() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "50")
                .param("max", "60")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByQuantity_invalidRange_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "10")
                .param("max", "5")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Min cannot be greater than max"));
    }

    @Test
    void getTotalSpent_existingCustomer_returnsCalculatedTotal() throws Exception {
        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());
        UUID customerId = customer.getId();

        // Act
        purchaseRecordRepository.save(
            createPurchaseRecord(
                customer,
                UUID.randomUUID(),
                2,
                10.0,
                LocalDateTime.of(2026, 3, 6, 8, 0)
            )
        );

        purchaseRecordRepository.save(
            createPurchaseRecord(
                customer,
                UUID.randomUUID(),
                3,
                5.5,
                LocalDateTime.of(2026, 3, 6, 9, 0)
            )
        );

        // Act + Assert
        mockMvc.perform(get("/purchases/customer/{customerId}/total-spent", customerId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", closeTo(36.5, 0.001)));
    }

    @Test
    void deletePurchase_existingPurchase_returnsNoContent() throws Exception {
        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord purchase = purchaseRecordRepository.save(
            createPurchaseRecord(
                customer,
                UUID.randomUUID(),
                2,
                7.0,
                LocalDateTime.of(2026, 3, 7, 12, 0)
            )
        );

        // Act + Assert
        mockMvc.perform(delete("/purchases/{id}", purchase.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    void deletePurchase_nonExistingPurchase_returnsNotFound() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(delete("/purchases/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void getPurchase_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByCustomerId_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/customer/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByWineId_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/wine/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getTotalSpent_invalidUuid_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/customer/not-a-uuid/total-spent"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByQuantity_invalidMinType_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "abc")
                .param("max", "5"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByQuantity_invalidMaxType_returnsBadRequest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "1")
                .param("max", "xyz"))
            .andExpect(status().isBadRequest());
    }

    private PurchaseRecord createPurchaseRecord(Customer customer, UUID wineId, int quantity, double pricePerBottle, LocalDateTime purchaseDate) {
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setCustomer(customer);
        purchaseRecord.setWineId(wineId);
        purchaseRecord.setQuantity(quantity);
        purchaseRecord.setPricePerBottle(pricePerBottle);
        purchaseRecord.setPurchaseDate(purchaseDate);
        return purchaseRecord;
    }

    private String validCreatePurchaseJson(UUID customerId, UUID wineId, int quantity, double pricePerBottle) {
        return """
            {
              "customerId": "%s",
              "wineId": "%s",
              "quantity": %d,
              "pricePerBottle": %s
            }
            """.formatted(customerId, wineId, quantity, pricePerBottle);
    }
}
