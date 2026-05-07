package cz.muni.fi.pa165.inventoryservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.inventoryservice.InventoryServiceApplication;
import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.api.StockQuantityChangeDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = InventoryServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
class StockIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createStock_persistsEntityAndReturnsItThroughApi() throws Exception {
        UUID wineId = UUID.randomUUID();

        StockDetailedViewDto createDto = new StockDetailedViewDto();
        createDto.setId(UUID.randomUUID());
        createDto.setWineId(wineId);
        createDto.setQuantity(120);
        createDto.setReservedQuantity(20);

        MvcResult createResult = mockMvc.perform(post("/stocks")
                .with(jwt().jwt(jwt -> jwt.claim("scope", "test_write")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.quantity").value(120))
            .andExpect(jsonPath("$.reservedQuantity").value(20))
            .andReturn();

        StockDetailedViewDto created = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            StockDetailedViewDto.class);

        Stock persisted = stockRepository.findById(created.getId()).orElseThrow();
        assertThat(persisted.getWineId()).isEqualTo(wineId);
        assertThat(persisted.getQuantity()).isEqualTo(120);
        assertThat(persisted.getReservedQuantity()).isEqualTo(20);

        mockMvc.perform(get("/stocks/{id}", created.getId())
                .with(jwt().jwt(jwt -> jwt.claim("scope", "test_read")))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(created.getId().toString()))
            .andExpect(jsonPath("$.wineId").value(wineId.toString()));
    }

    @Test
    void addToStock_increasesQuantityInDatabase() throws Exception {
        Stock stock = new Stock();
        stock.setWineId(UUID.randomUUID());
        stock.setQuantity(50);
        stock.setReservedQuantity(0);
        stock = stockRepository.saveAndFlush(stock);

        StockQuantityChangeDto change = new StockQuantityChangeDto();
        change.setQuantity(15);

        mockMvc.perform(post("/stocks/{id}/add", stock.getId())
                .with(jwt().jwt(jwt -> jwt.claim("scope", "test_write")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(change)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(65));

        Stock refreshed = stockRepository.findById(stock.getId()).orElseThrow();
        assertThat(refreshed.getQuantity()).isEqualTo(65);
    }
}
