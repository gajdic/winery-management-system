package cz.muni.fi.pa165.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.api.StockQuantityChangeDto;
import cz.muni.fi.pa165.inventoryservice.config.TestJacksonConfig;
import cz.muni.fi.pa165.inventoryservice.facade.StockFacade;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockController.class)
@Import(TestJacksonConfig.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockFacade stockFacade;

    @Test
    void findById_stockFound_returnsStock() throws Exception {
        // Arrange
        StockDetailedViewDto dto = TestDataFactory.stockDetailedViewDto();
        Mockito.when(stockFacade.findById(TestDataFactory.STOCK_ID)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/stocks/{id}", TestDataFactory.STOCK_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.STOCK_ID.toString()))
            .andExpect(jsonPath("$.wineId").value(TestDataFactory.WINE_ID.toString()))
            .andExpect(jsonPath("$.quantity").value(120))
            .andExpect(jsonPath("$.reservedQuantity").value(20));
    }

    @Test
    void findById_stockNotFound_returnsNotFound() throws Exception {
        // Arrange
        Mockito.when(stockFacade.findById(TestDataFactory.STOCK_ID)).thenReturn(null);

        // Act + Assert
        mockMvc.perform(get("/stocks/{id}", TestDataFactory.STOCK_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAll_stocksExist_returnsStocks() throws Exception {
        // Arrange
        List<StockDetailedViewDto> dtos = TestDataFactory.stockDetailedViewDtos();
        Mockito.when(stockFacade.findAll()).thenReturn(dtos);

        // Act + Assert
        mockMvc.perform(get("/stocks")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(TestDataFactory.STOCK_ID.toString()));
    }

    @Test
    void addToStock_validQuantity_returnsUpdatedStock() throws Exception {
        // Arrange
        StockQuantityChangeDto changeDto = TestDataFactory.stockQuantityChangeDto(10);
        StockDetailedViewDto updatedDto = TestDataFactory.stockDetailedViewDto(130, 20);
        Mockito.when(stockFacade.addToStock(TestDataFactory.STOCK_ID, 10)).thenReturn(updatedDto);

        // Act + Assert
        mockMvc.perform(post("/stocks/{id}/add", TestDataFactory.STOCK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(130));
    }

    @Test
    void addToStock_invalidQuantity_returnsBadRequest() throws Exception {
        // Arrange
        StockQuantityChangeDto changeDto = TestDataFactory.stockQuantityChangeDto(0);
        Mockito.when(stockFacade.addToStock(TestDataFactory.STOCK_ID, 0))
            .thenThrow(new IllegalArgumentException("Quantity must be positive"));

        // Act + Assert
        mockMvc.perform(post("/stocks/{id}/add", TestDataFactory.STOCK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void removeFromStock_stockNotFound_returnsNotFound() throws Exception {
        // Arrange
        StockQuantityChangeDto changeDto = TestDataFactory.stockQuantityChangeDto(10);
        Mockito.when(stockFacade.removeFromStock(TestDataFactory.STOCK_ID, 10))
            .thenThrow(new ResourceNotFoundException("Stock not found: " + TestDataFactory.STOCK_ID));

        // Act + Assert
        mockMvc.perform(post("/stocks/{id}/remove", TestDataFactory.STOCK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void delete_stockDeleted_returnsNoContent() throws Exception {
        // Arrange
        Mockito.when(stockFacade.delete(TestDataFactory.STOCK_ID)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/stocks/{id}", TestDataFactory.STOCK_ID))
            .andExpect(status().isNoContent());
    }

    @Test
    void update_validStock_returnsUpdatedStock() throws Exception {
        // Arrange
        StockDetailedViewDto dto = TestDataFactory.stockDetailedViewDto();
        Mockito.when(stockFacade.update(Mockito.any(StockDetailedViewDto.class))).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(put("/stocks/{id}", TestDataFactory.STOCK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.STOCK_ID.toString()));
    }
}
