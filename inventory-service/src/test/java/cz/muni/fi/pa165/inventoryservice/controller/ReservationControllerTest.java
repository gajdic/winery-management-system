package cz.muni.fi.pa165.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.config.JpaConfig;
import cz.muni.fi.pa165.inventoryservice.config.SecurityConfig;
import cz.muni.fi.pa165.inventoryservice.config.TestJacksonConfig;
import cz.muni.fi.pa165.inventoryservice.facade.ReservationFacade;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = ReservationController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class)
)
@Import({TestJacksonConfig.class, SecurityConfig.class})
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationFacade reservationFacade;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void findById_reservationFound_returnsReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationFacade.findById(TestDataFactory.RESERVATION_ID)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(token -> token.claim("scope", "test_read"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.RESERVATION_ID.toString()))
            .andExpect(jsonPath("$.wineId").value(TestDataFactory.WINE_ID.toString()))
            .andExpect(jsonPath("$.customerId").value(TestDataFactory.CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    void findById_reservationNotFound_returnsNotFound() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.findById(TestDataFactory.RESERVATION_ID)).thenReturn(null);

        // Act + Assert
        mockMvc.perform(get("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(token -> token.claim("scope", "test_read"))))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAll_reservationsExist_returnsReservations() throws Exception {
        // Arrange
        List<ReservationBasicViewDto> dtos = TestDataFactory.reservationBasicViewDtos();
        Mockito.when(reservationFacade.findAll()).thenReturn(dtos);

        // Act + Assert
        mockMvc.perform(get("/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(token -> token.claim("scope", "test_read"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(TestDataFactory.RESERVATION_ID.toString()));
    }

    @Test
    void create_validReservation_returnsCreatedReservation() throws Exception {
        // Arrange
        ReservationDetailedViewDto dto = TestDataFactory.reservationDetailedViewDto();
        Mockito.when(reservationFacade.create(Mockito.any(ReservationDetailedViewDto.class))).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(jwt().jwt(token -> token.claim("scope", "test_write"))))
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
                .content(objectMapper.writeValueAsString(dto))
                .with(jwt().jwt(token -> token.claim("scope", "test_write"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestDataFactory.RESERVATION_ID.toString()));
    }

    @Test
    void delete_reservationDeleted_returnsNoContent() throws Exception {
        // Arrange
        Mockito.when(reservationFacade.delete(TestDataFactory.RESERVATION_ID)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/reservations/{id}", TestDataFactory.RESERVATION_ID)
                .with(jwt().jwt(token -> token.claim("scope", "test_write"))))
            .andExpect(status().isNoContent());
    }
}
