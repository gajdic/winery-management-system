package cz.muni.fi.pa165.harvestservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.harvestservice.HarvestServiceApplication;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HarvestServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
class VineyardIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VineyardRepository vineyardRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createVineyard_persistsEntityAndReturnsItThroughApi() throws Exception {
        VineyardCreateDto createDto = new VineyardCreateDto(
            "Mikulov Hills",
            "Mikulov",
            12.5,
            "Sunny South Moravian vineyard"
        );

        MvcResult createResult = mockMvc.perform(post("/vineyards")
                .with(jwt().jwt(token -> token.claim("scope", "test_write")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Mikulov Hills"))
            .andExpect(jsonPath("$.location").value("Mikulov"))
            .andExpect(jsonPath("$.areaSize").value(12.5))
            .andExpect(jsonPath("$.description").value("Sunny South Moravian vineyard"))
            .andReturn();

        VineyardViewDto createdVineyard = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            VineyardViewDto.class
        );

        Vineyard persistedVineyard = vineyardRepository.findById(createdVineyard.getId()).orElseThrow();
        assertThat(persistedVineyard.getName()).isEqualTo("Mikulov Hills");
        assertThat(persistedVineyard.getLocation()).isEqualTo("Mikulov");
        assertThat(persistedVineyard.getAreaSize()).isEqualTo(12.5);
        assertThat(persistedVineyard.getDescription()).isEqualTo("Sunny South Moravian vineyard");

        mockMvc.perform(get("/vineyards/{id}", createdVineyard.getId())
                .with(jwt().jwt(token -> token.claim("scope", "test_read")))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdVineyard.getId().toString()))
            .andExpect(jsonPath("$.name").value("Mikulov Hills"))
            .andExpect(jsonPath("$.location").value("Mikulov"))
            .andExpect(jsonPath("$.areaSize").value(12.5))
            .andExpect(jsonPath("$.description").value("Sunny South Moravian vineyard"));
    }
}
