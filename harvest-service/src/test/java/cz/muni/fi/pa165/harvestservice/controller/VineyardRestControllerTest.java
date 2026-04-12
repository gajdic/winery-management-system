package cz.muni.fi.pa165.harvestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.facade.VineyardFacade;
import cz.muni.fi.pa165.harvestservice.rest.VineyardRestController;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {VineyardRestController.class})
class VineyardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private VineyardFacade vineyardFacade;

    @Test
    void create_validDto_returnsCreatedVineyard() throws Exception {
        // Arrange
        VineyardCreateDto createDto = TestDataFactory.vineyardCreateDto();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();
        Mockito.when(vineyardFacade.create(Mockito.any(VineyardCreateDto.class))).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(post("/vineyards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TestDataFactory.VINEYARD_ID.toString()))
                .andExpect(jsonPath("$.name").value("Moravské vinice"))
                .andExpect(jsonPath("$.location").value("Brno, Morava"))
                .andExpect(jsonPath("$.areaSize").value(5.5));
    }

    @Test
    void create_blankName_returnsBadRequest() throws Exception {
        // Arrange
        VineyardCreateDto createDto = new VineyardCreateDto("", "Brno, Morava", 5.5, "popis");

        // Act & Assert
        mockMvc.perform(post("/vineyards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_negativeAreaSize_returnsBadRequest() throws Exception {
        // Arrange
        VineyardCreateDto createDto = new VineyardCreateDto("Vinice", "Brno", -1.0, "popis");

        // Act & Assert
        mockMvc.perform(post("/vineyards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_vineyardFound_returnsOk() throws Exception {
        // Arrange
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();
        Mockito.when(vineyardFacade.findById(TestDataFactory.VINEYARD_ID)).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(get("/vineyards/{id}", TestDataFactory.VINEYARD_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TestDataFactory.VINEYARD_ID.toString()))
                .andExpect(jsonPath("$.name").value("Moravské vinice"));
    }

    @Test
    void findById_vineyardNotFound_returnsNotFound() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        Mockito.when(vineyardFacade.findById(unknownId))
                .thenThrow(new ResourceNotFoundException("Vineyard with id: " + unknownId + " was not found."));

        // Act & Assert
        mockMvc.perform(get("/vineyards/{id}", unknownId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_vineyardFound_returnsOk() throws Exception {
        // Arrange
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();
        Mockito.when(vineyardFacade.findByName("Moravské vinice")).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(get("/vineyards/by-name/{name}", "Moravské vinice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Moravské vinice"));
    }

    @Test
    void findAll_vineyardsExist_returnsOkWithList() throws Exception {
        // Arrange
        List<VineyardViewDto> viewDtos = List.of(TestDataFactory.vineyardViewDto());
        Mockito.when(vineyardFacade.findAll()).thenReturn(viewDtos);

        // Act & Assert
        mockMvc.perform(get("/vineyards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Moravské vinice"));
    }

    @Test
    void update_validDto_returnsUpdatedVineyard() throws Exception {
        // Arrange
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();
        Mockito.when(vineyardFacade.update(Mockito.eq(TestDataFactory.VINEYARD_ID), Mockito.any()))
                .thenReturn(viewDto);

        String updateJson = """
                {
                    "name": "Moravské vinice",
                    "location": "Brno, Morava",
                    "areaSize": 5.5,
                    "description": "Nejstarší vinice na Moravě"
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/vineyards/{id}", TestDataFactory.VINEYARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Moravské vinice"));
    }

    @Test
    void delete_existingVineyard_returnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/vineyards/{id}", TestDataFactory.VINEYARD_ID))
                .andExpect(status().isNoContent());

        Mockito.verify(vineyardFacade).deleteById(TestDataFactory.VINEYARD_ID);
    }
}
