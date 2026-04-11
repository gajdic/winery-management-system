package cz.muni.fi.pa165.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.config.TestJacksonConfig;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.facade.ReservationFacade;
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

@WebMvcTest(controllers = ReservationController.class)
@Import(TestJacksonConfig.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationFacade reservationFacade;

    @Test
    void findById_reservationFound_returnsReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationFacade.findById(TestDataFactory.RESERVATION_ID)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.RESERVATION_ID.toString()))
            .andExpect(jsonPath("$.wineId").value(TestDataFactory.WINE_ID.toString()))
            .andExpect(jsonPath("$.customerId").value(TestDataFactory.CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.quantity").value(15))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void findById_reservationNotFound_returnsNotFound() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.findById(TestDataFactory.RESERVATION_ID)).thenReturn(null);

        // Act + Assert
        mockMvc.perform(get("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAll_reservationsExist_returnsReservations() throws Exception {
        // Arrange
        List<ReservationBasicViewDto> dtos = TestDataFactory.reservationBasicViewDtos();
        Mockito.when(reservationFacade.findAll()).thenReturn(dtos);

        // Act + Assert
        mockMvc.perform(get("/reservations")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(TestDataFactory.RESERVATION_ID.toString()));
    }

    @Test
    void updateStatus_validStatus_returnsUpdatedReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto(ReservationStatus.COMPLETED, 15);
        Mockito.when(reservationFacade.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(patch("/reservations/{id}/status", TestDataFactory.RESERVATION_ID)
                .param("status", "COMPLETED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateStatus_reservationNotFound_returnsNotFound() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.COMPLETED))
            .thenThrow(new ResourceNotFoundException("Reservation not found: " + TestDataFactory.RESERVATION_ID));

        // Act + Assert
        mockMvc.perform(patch("/reservations/{id}/status", TestDataFactory.RESERVATION_ID)
                .param("status", "COMPLETED"))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateStatus_invalidStatusTransition_returnsBadRequest() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.updateStatus(TestDataFactory.RESERVATION_ID, ReservationStatus.CANCELED))
            .thenThrow(new IllegalArgumentException("Status must not be null"));

        // Act + Assert
        mockMvc.perform(patch("/reservations/{id}/status", TestDataFactory.RESERVATION_ID)
                .param("status", "CANCELED"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_validReservation_returnsCreatedReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationFacade.create(Mockito.any(ReservationDetailedViewDto.class))).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(TestDataFactory.RESERVATION_ID.toString()));
    }

    @Test
    void update_validReservation_returnsUpdatedReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationFacade.update(Mockito.any(ReservationDetailedViewDto.class))).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(put("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.RESERVATION_ID.toString()));
    }

    @Test
    void delete_reservationDeleted_returnsNoContent() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.delete(TestDataFactory.RESERVATION_ID)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/reservations/{id}", TestDataFactory.RESERVATION_ID))
            .andExpect(status().isNoContent());
    }
}
