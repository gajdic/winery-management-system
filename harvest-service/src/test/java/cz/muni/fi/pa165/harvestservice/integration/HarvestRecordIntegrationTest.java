package cz.muni.fi.pa165.harvestservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.harvestservice.HarvestServiceApplication;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.HarvestRecordRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HarvestServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
class HarvestRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private GrapeTypeRepository grapeTypeRepository;

    @Autowired
    private VineyardRepository vineyardRepository;

    @Autowired
    private HarvestRecordRepository harvestRecordRepository;

    @Test
    void createHarvestRecord_persistsRecordAndReturnsItThroughApi() throws Exception {
        GrapeType grapeType = new GrapeType();
        grapeType.setName("Riesling");
        grapeType.setDescription("Fresh white variety");
        grapeType = grapeTypeRepository.saveAndFlush(grapeType);

        Vineyard vineyard = new Vineyard();
        vineyard.setName("Sonberk");
        vineyard.setLocation("Popice");
        vineyard.setAreaSize(45.0);
        vineyard.setDescription("South Moravian vineyard");
        vineyard = vineyardRepository.saveAndFlush(vineyard);

        HarvestRecordCreateDto createDto = new HarvestRecordCreateDto(
            grapeType.getId(),
            vineyard.getId(),
            LocalDate.of(2024, 9, 15),
            1500.0,
            8
        );

        MvcResult createResult = mockMvc.perform(post("/harvest-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.grapeType.id").value(grapeType.getId().toString()))
            .andExpect(jsonPath("$.grapeType.name").value("Riesling"))
            .andExpect(jsonPath("$.vineyard.id").value(vineyard.getId().toString()))
            .andExpect(jsonPath("$.vineyard.name").value("Sonberk"))
            .andExpect(jsonPath("$.harvestDate").value("2024-09-15"))
            .andExpect(jsonPath("$.quantity").value(1500.0))
            .andExpect(jsonPath("$.quality").value(8))
            .andReturn();

        HarvestRecordViewDto createdRecord = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            HarvestRecordViewDto.class
        );

        HarvestRecord persistedRecord = harvestRecordRepository.findById(createdRecord.getId()).orElseThrow();
        assertThat(persistedRecord.getHarvestDate()).isEqualTo(LocalDate.of(2024, 9, 15));
        assertThat(persistedRecord.getQuantity()).isEqualTo(1500.0);
        assertThat(persistedRecord.getQuality()).isEqualTo(8.0);
        assertThat(persistedRecord.getGrapeType().getId()).isEqualTo(grapeType.getId());
        assertThat(persistedRecord.getVineyard().getId()).isEqualTo(vineyard.getId());

        mockMvc.perform(get("/harvest-records/{id}", createdRecord.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdRecord.getId().toString()))
            .andExpect(jsonPath("$.grapeType.name").value("Riesling"))
            .andExpect(jsonPath("$.vineyard.name").value("Sonberk"))
            .andExpect(jsonPath("$.quantity").value(1500.0))
            .andExpect(jsonPath("$.quality").value(8));
    }
}
