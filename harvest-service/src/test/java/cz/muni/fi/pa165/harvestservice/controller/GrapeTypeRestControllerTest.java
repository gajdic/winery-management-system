package cz.muni.fi.pa165.harvestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.facade.GrapeTypeFacade;
import cz.muni.fi.pa165.harvestservice.rest.GrapeTypeRestController;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GrapeTypeRestController.class})
@AutoConfigureMockMvc(addFilters = false)
class GrapeTypeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private GrapeTypeFacade grapeTypeFacade;

    @Test
    void create_validDto_returnsCreatedGrapeType() throws Exception {
        // Arrange
        GrapeTypeCreateDto createDto = TestDataFactory.grapeTypeCreateDto();
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();
        Mockito.when(grapeTypeFacade.create(Mockito.any(GrapeTypeCreateDto.class))).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(post("/grape-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TestDataFactory.GRAPE_TYPE_ID.toString()))
                .andExpect(jsonPath("$.name").value("Welschriesling"))
                .andExpect(jsonPath("$.description").value("Oblíbená odrůda na Moravě"));
    }

    @Test
    void create_blankName_returnsBadRequest() throws Exception {
        // Arrange
        GrapeTypeCreateDto createDto = new GrapeTypeCreateDto("", "popis");

        // Act & Assert
        mockMvc.perform(post("/grape-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_grapeTypeFound_returnsOk() throws Exception {
        // Arrange
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();
        Mockito.when(grapeTypeFacade.findById(TestDataFactory.GRAPE_TYPE_ID)).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(get("/grape-types/{id}", TestDataFactory.GRAPE_TYPE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TestDataFactory.GRAPE_TYPE_ID.toString()))
                .andExpect(jsonPath("$.name").value("Welschriesling"));
    }

    @Test
    void findById_grapeTypeNotFound_returnsNotFound() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        Mockito.when(grapeTypeFacade.findById(unknownId))
                .thenThrow(new ResourceNotFoundException("Grape type with id: " + unknownId + " was not found."));

        // Act & Assert
        mockMvc.perform(get("/grape-types/{id}", unknownId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_grapeTypeFound_returnsOk() throws Exception {
        // Arrange
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();
        Mockito.when(grapeTypeFacade.findByName("Welschriesling")).thenReturn(viewDto);

        // Act & Assert
        mockMvc.perform(get("/grape-types/by-name/{name}", "Welschriesling")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Welschriesling"));
    }

    @Test
    void findAll_grapeTypesExist_returnsOkWithList() throws Exception {
        // Arrange
        List<GrapeTypeViewDto> viewDtos = List.of(TestDataFactory.grapeTypeViewDto());
        Mockito.when(grapeTypeFacade.findAll()).thenReturn(viewDtos);

        // Act & Assert
        mockMvc.perform(get("/grape-types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Welschriesling"));
    }

    @Test
    void update_validDto_returnsUpdatedGrapeType() throws Exception {
        // Arrange
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();
        Mockito.when(grapeTypeFacade.update(Mockito.eq(TestDataFactory.GRAPE_TYPE_ID), Mockito.any()))
                .thenReturn(viewDto);

        String updateJson = """
                {
                    "name": "Welschriesling",
                    "description": "Oblíbená odrůda na Moravě"
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/grape-types/{id}", TestDataFactory.GRAPE_TYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Welschriesling"));
    }

    @Test
    void delete_existingGrapeType_returnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/grape-types/{id}", TestDataFactory.GRAPE_TYPE_ID))
                .andExpect(status().isNoContent());

        Mockito.verify(grapeTypeFacade).deleteById(TestDataFactory.GRAPE_TYPE_ID);
    }
}
