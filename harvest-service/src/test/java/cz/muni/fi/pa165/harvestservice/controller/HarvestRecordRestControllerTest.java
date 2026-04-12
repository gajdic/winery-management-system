package cz.muni.fi.pa165.harvestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.facade.HarvestRecordFacade;
import cz.muni.fi.pa165.harvestservice.rest.HarvestRecordRestController;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {HarvestRecordRestController.class})
class HarvestRecordRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private HarvestRecordFacade harvestRecordFacade;

    @Test
    void create_validDto_returnsCreatedRecord() throws Exception {
        HarvestRecordCreateDto createDto = TestDataFactory.harvestRecordCreateDto();
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();
        Mockito.when(harvestRecordFacade.create(Mockito.any(HarvestRecordCreateDto.class))).thenReturn(viewDto);

        mockMvc.perform(post("/harvest-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(TestDataFactory.HARVEST_RECORD_ID.toString()))
            .andExpect(jsonPath("$.grapeType.id").value(TestDataFactory.GRAPE_TYPE_ID.toString()))
            .andExpect(jsonPath("$.grapeType.name").value("Welschriesling"))
            .andExpect(jsonPath("$.vineyard.id").value(TestDataFactory.VINEYARD_ID.toString()))
            .andExpect(jsonPath("$.vineyard.name").value("Moravsk\u00e9 vinice"))
            .andExpect(jsonPath("$.quantity").value(1500.0))
            .andExpect(jsonPath("$.quality").value(8));
    }

    @Test
    void create_nullGrapeTypeId_returnsBadRequest() throws Exception {
        HarvestRecordCreateDto createDto = new HarvestRecordCreateDto(
            null, TestDataFactory.VINEYARD_ID, LocalDate.of(2024, 9, 15), 1500.0, 8
        );

        mockMvc.perform(post("/harvest-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_qualityAboveTen_returnsBadRequest() throws Exception {
        HarvestRecordCreateDto createDto = new HarvestRecordCreateDto(
            TestDataFactory.GRAPE_TYPE_ID, TestDataFactory.VINEYARD_ID,
            LocalDate.of(2024, 9, 15), 1500.0, 11
        );

        mockMvc.perform(post("/harvest-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_negativeQuantity_returnsBadRequest() throws Exception {
        HarvestRecordCreateDto createDto = new HarvestRecordCreateDto(
            TestDataFactory.GRAPE_TYPE_ID, TestDataFactory.VINEYARD_ID,
            LocalDate.of(2024, 9, 15), -100.0, 8
        );

        mockMvc.perform(post("/harvest-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findById_recordFound_returnsOk() throws Exception {
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();
        Mockito.when(harvestRecordFacade.findById(TestDataFactory.HARVEST_RECORD_ID)).thenReturn(viewDto);

        mockMvc.perform(get("/harvest-records/{id}", TestDataFactory.HARVEST_RECORD_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.HARVEST_RECORD_ID.toString()))
            .andExpect(jsonPath("$.quantity").value(1500.0));
    }

    @Test
    void findById_recordNotFound_returnsNotFound() throws Exception {
        UUID unknownId = UUID.randomUUID();
        Mockito.when(harvestRecordFacade.findById(unknownId))
            .thenThrow(new ResourceNotFoundException("Harvest record with id " + unknownId + " was not found."));

        mockMvc.perform(get("/harvest-records/{id}", unknownId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void findByYear_recordsExist_returnsOkWithList() throws Exception {
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());
        Mockito.when(harvestRecordFacade.findByYear(2024)).thenReturn(viewDtos);

        mockMvc.perform(get("/harvest-records/by-year/{year}", 2024)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].quantity").value(1500.0));
    }

    @Test
    void findByVineyardId_recordsExist_returnsOkWithList() throws Exception {
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());
        Mockito.when(harvestRecordFacade.findByVineyardId(TestDataFactory.VINEYARD_ID)).thenReturn(viewDtos);

        mockMvc.perform(get("/harvest-records/by-vineyard/{id}", TestDataFactory.VINEYARD_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].vineyard.name").value("Moravsk\u00e9 vinice"));
    }

    @Test
    void findByGrapeTypeId_recordsExist_returnsOkWithList() throws Exception {
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());
        Mockito.when(harvestRecordFacade.findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID)).thenReturn(viewDtos);

        mockMvc.perform(get("/harvest-records/by-grape-type/{id}", TestDataFactory.GRAPE_TYPE_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].grapeType.name").value("Welschriesling"));
    }

    @Test
    void findAll_recordsExist_returnsOkWithList() throws Exception {
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());
        Mockito.when(harvestRecordFacade.findAll()).thenReturn(viewDtos);

        mockMvc.perform(get("/harvest-records")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void summaryByYear_recordsExist_returnsOkWithSummary() throws Exception {
        List<HarvestSummaryDto> summaries = List.of(new HarvestSummaryDto("2024", 1500.0));
        Mockito.when(harvestRecordFacade.summaryByYear()).thenReturn(summaries);

        mockMvc.perform(get("/harvest-records/summary/by-year")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].groupByValue").value("2024"))
            .andExpect(jsonPath("$[0].totalQuantity").value(1500.0));
    }

    @Test
    void summaryByVineyard_recordsExist_returnsOkWithSummary() throws Exception {
        List<HarvestSummaryDto> summaries = List.of(
            new HarvestSummaryDto(TestDataFactory.VINEYARD_ID.toString(), 1500.0)
        );
        Mockito.when(harvestRecordFacade.summaryByVineyard()).thenReturn(summaries);

        mockMvc.perform(get("/harvest-records/summary/by-vineyard")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].totalQuantity").value(1500.0));
    }

    @Test
    void update_validDto_returnsUpdatedRecord() throws Exception {
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();
        Mockito.when(harvestRecordFacade.update(Mockito.eq(TestDataFactory.HARVEST_RECORD_ID), Mockito.any()))
            .thenReturn(viewDto);

        String updateJson = """
                {
                    "grapeTypeId": "%s",
                    "vineyardId": "%s",
                    "harvestDate": "2024-09-15",
                    "quantity": 1500.0,
                    "quality": 8
                }
                """.formatted(TestDataFactory.GRAPE_TYPE_ID, TestDataFactory.VINEYARD_ID);

        mockMvc.perform(put("/harvest-records/{id}", TestDataFactory.HARVEST_RECORD_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(1500.0));
    }

    @Test
    void delete_existingRecord_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/harvest-records/{id}", TestDataFactory.HARVEST_RECORD_ID))
            .andExpect(status().isNoContent());

        Mockito.verify(harvestRecordFacade).deleteById(TestDataFactory.HARVEST_RECORD_ID);
    }
}
