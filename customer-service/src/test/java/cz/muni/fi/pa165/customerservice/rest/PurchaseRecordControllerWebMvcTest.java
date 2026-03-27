package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.facade.PurchaseRecordFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.closeTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PurchaseRecordController.class)
class PurchaseRecordControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchaseRecordFacade purchaseRecordFacade;

    @Test
    void createPurchase_validRequest_returnsCreatedPurchase() throws Exception {
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 4, 12.0, LocalDateTime.of(2026, 3, 10, 11, 0));
        when(purchaseRecordFacade.createPurchase(any())).thenReturn(dto);

        mockMvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreatePurchaseRequestJson(dto.getCustomerId(), dto.getWineId(), 4, 12.0)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(dto.getId().toString()))
            .andExpect(jsonPath("$.customerId").value(dto.getCustomerId().toString()))
            .andExpect(jsonPath("$.wineId").value(dto.getWineId().toString()))
            .andExpect(jsonPath("$.quantity").value(4))
            .andExpect(jsonPath("$.pricePerBottle").value(12.0));
    }

    @Test
    void createPurchase_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCreatePurchaseRequestJson()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getPurchase_existingId_returnsPurchase() throws Exception {
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2, 7.5, LocalDateTime.of(2026, 3, 10, 12, 0));
        when(purchaseRecordFacade.getPurchase(dto.getId())).thenReturn(dto);

        mockMvc.perform(get("/purchases/{id}", dto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(dto.getId().toString()))
            .andExpect(jsonPath("$.customerId").value(dto.getCustomerId().toString()))
            .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void getPurchase_nonExistingId_returnsNotFound() throws Exception {
        UUID purchaseId = UUID.randomUUID();
        when(purchaseRecordFacade.getPurchase(purchaseId))
            .thenThrow(new IllegalArgumentException("PurchaseRecord not found: " + purchaseId));

        mockMvc.perform(get("/purchases/{id}", purchaseId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("PurchaseRecord not found: " + purchaseId));
    }

    @Test
    void getAllPurchases_returnsAllPurchases() throws Exception {
        PurchaseRecordDto first = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 1, 5.0, LocalDateTime.of(2026, 3, 10, 13, 0));
        PurchaseRecordDto second = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 3, 8.0, LocalDateTime.of(2026, 3, 10, 14, 0));
        when(purchaseRecordFacade.getAllPurchases()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/purchases"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(first.getId().toString()))
            .andExpect(jsonPath("$[1].id").value(second.getId().toString()));
    }

    @Test
    void getAllPurchases_emptyList_returnsEmptyArray() throws Exception {
        when(purchaseRecordFacade.getAllPurchases()).thenReturn(List.of());

        mockMvc.perform(get("/purchases"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByCustomerId_returnsMatchingPurchases() throws Exception {
        UUID customerId = UUID.randomUUID();
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), customerId, UUID.randomUUID(), 2, 9.0, LocalDateTime.of(2026, 3, 10, 15, 0));
        when(purchaseRecordFacade.getByCustomerId(customerId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/purchases/customer/{customerId}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].customerId").value(customerId.toString()));
    }

    @Test
    void getByCustomerId_nonExistingCustomer_returnsEmptyArray() throws Exception {
        UUID customerId = UUID.randomUUID();
        when(purchaseRecordFacade.getByCustomerId(customerId)).thenReturn(List.of());

        mockMvc.perform(get("/purchases/customer/{customerId}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByWineId_returnsMatchingPurchases() throws Exception {
        UUID wineId = UUID.randomUUID();
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), wineId, 2, 9.0, LocalDateTime.of(2026, 3, 10, 16, 0));
        when(purchaseRecordFacade.getByWineId(wineId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/purchases/wine/{wineId}", wineId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].wineId").value(wineId.toString()));
    }

    @Test
    void getByWineId_nonExistingWine_returnsEmptyArray() throws Exception {
        UUID wineId = UUID.randomUUID();
        when(purchaseRecordFacade.getByWineId(wineId)).thenReturn(List.of());

        mockMvc.perform(get("/purchases/wine/{wineId}", wineId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByQuantity_returnsMatchingPurchases() throws Exception {
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 4, 9.0, LocalDateTime.of(2026, 3, 10, 17, 0));
        when(purchaseRecordFacade.getByQuantity(2, 5)).thenReturn(List.of(dto));

        mockMvc.perform(get("/purchases/quantity")
                .param("min", "2")
                .param("max", "5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].quantity").value(4));
    }

    @Test
    void getByQuantity_onlyMinProvided_usesDefaultMax() throws Exception {
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 6, 9.0, LocalDateTime.of(2026, 3, 10, 17, 30));
        when(purchaseRecordFacade.getByQuantity(5, null)).thenReturn(List.of(dto));

        mockMvc.perform(get("/purchases/quantity")
                .param("min", "5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].quantity").value(6));
    }

    @Test
    void getByQuantity_onlyMaxProvided_usesDefaultMin() throws Exception {
        PurchaseRecordDto dto = createPurchaseDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 1, 9.0, LocalDateTime.of(2026, 3, 10, 17, 45));
        when(purchaseRecordFacade.getByQuantity(null, 2)).thenReturn(List.of(dto));

        mockMvc.perform(get("/purchases/quantity")
                .param("max", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].quantity").value(1));
    }

    @Test
    void getByQuantity_nonExistingRange_returnsEmptyArray() throws Exception {
        when(purchaseRecordFacade.getByQuantity(50, 60)).thenReturn(List.of());

        mockMvc.perform(get("/purchases/quantity")
                .param("min", "50")
                .param("max", "60"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByQuantity_invalidRange_returnsBadRequest() throws Exception {
        when(purchaseRecordFacade.getByQuantity(10, 5)).thenThrow(new IllegalArgumentException("Min cannot be greater than max"));

        mockMvc.perform(get("/purchases/quantity")
                .param("min", "10")
                .param("max", "5"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Min cannot be greater than max"));
    }

    @Test
    void getTotalSpent_returnsTotal() throws Exception {
        UUID customerId = UUID.randomUUID();
        when(purchaseRecordFacade.getTotalSpentByCustomer(customerId)).thenReturn(42.5);

        mockMvc.perform(get("/purchases/customer/{customerId}/total-spent", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", closeTo(42.5, 0.001)));
    }

    @Test
    void deletePurchase_existingId_returnsNoContent() throws Exception {
        UUID purchaseId = UUID.randomUUID();
        doNothing().when(purchaseRecordFacade).deletePurchase(purchaseId);

        mockMvc.perform(delete("/purchases/{id}", purchaseId))
            .andExpect(status().isNoContent());

        verify(purchaseRecordFacade).deletePurchase(purchaseId);
    }

    @Test
    void getPurchase_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByCustomerId_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/customer/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByWineId_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/wine/not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getTotalSpent_invalidUuid_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/customer/not-a-uuid/total-spent"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByQuantity_invalidMinType_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "abc")
                .param("max", "5"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getByQuantity_invalidMaxType_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/purchases/quantity")
                .param("min", "1")
                .param("max", "xyz"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getPurchase_unexpectedError_returnsInternalServerError() throws Exception {
        UUID purchaseId = UUID.randomUUID();
        when(purchaseRecordFacade.getPurchase(purchaseId)).thenThrow(new RuntimeException("Unexpected purchase failure"));

        mockMvc.perform(get("/purchases/{id}", purchaseId))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value("500 INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("Unexpected purchase failure"));
    }

    private PurchaseRecordDto createPurchaseDto(UUID id, UUID customerId, UUID wineId, int quantity, double pricePerBottle, LocalDateTime purchaseDate) {
        PurchaseRecordDto dto = new PurchaseRecordDto();
        dto.setId(id);
        dto.setCustomerId(customerId);
        dto.setWineId(wineId);
        dto.setQuantity(quantity);
        dto.setPricePerBottle(pricePerBottle);
        dto.setPurchaseDate(purchaseDate);
        return dto;
    }

    private String validCreatePurchaseRequestJson(UUID customerId, UUID wineId, int quantity, double pricePerBottle) {
        return """
            {
              "customerId": "%s",
              "wineId": "%s",
              "quantity": %d,
              "pricePerBottle": %s
            }
            """.formatted(customerId, wineId, quantity, pricePerBottle);
    }

    private String invalidCreatePurchaseRequestJson() {
        return """
            {
              "customerId": null,
              "wineId": null,
              "quantity": 0,
              "pricePerBottle": -1
            }
            """;
    }
}
