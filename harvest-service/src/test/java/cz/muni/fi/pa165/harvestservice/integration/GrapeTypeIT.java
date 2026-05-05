package cz.muni.fi.pa165.harvestservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.harvestservice.HarvestServiceApplication;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
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
class GrapeTypeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GrapeTypeRepository grapeTypeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createGrapeType_persistsEntityAndReturnsItThroughApi() throws Exception {
        GrapeTypeCreateDto createDto = new GrapeTypeCreateDto("Rulandske sede", "Aromatic white grape");

        MvcResult createResult = mockMvc.perform(post("/grape-types")
                .with(jwt().jwt(token -> token.claim("scope", "test_write")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Rulandske sede"))
            .andExpect(jsonPath("$.description").value("Aromatic white grape"))
            .andReturn();

        GrapeTypeViewDto createdGrapeType = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            GrapeTypeViewDto.class
        );

        GrapeType persistedGrapeType = grapeTypeRepository.findById(createdGrapeType.getId()).orElseThrow();
        assertThat(persistedGrapeType.getName()).isEqualTo("Rulandske sede");
        assertThat(persistedGrapeType.getDescription()).isEqualTo("Aromatic white grape");

        mockMvc.perform(get("/grape-types/{id}", createdGrapeType.getId())
                .with(jwt().jwt(token -> token.claim("scope", "test_read")))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdGrapeType.getId().toString()))
            .andExpect(jsonPath("$.name").value("Rulandske sede"))
            .andExpect(jsonPath("$.description").value("Aromatic white grape"));
    }
}
