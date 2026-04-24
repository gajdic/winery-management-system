package cz.muni.fi.pa165.inventoryservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.inventoryservice.InventoryServiceApplication;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = InventoryServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
class ReservationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createReservation_persistsEntityAndReturnsItThroughApi() throws Exception {
        UUID customerId = UUID.randomUUID();
        UUID wineId = UUID.randomUUID();

        ReservationDetailedViewDto createDto = new ReservationDetailedViewDto();
        createDto.setId(UUID.randomUUID());
        createDto.setCustomerId(customerId);
        createDto.setWineId(wineId);
        createDto.setQuantity(6);
        createDto.setCreatedAt(LocalDate.of(2025, 10, 15));

        MvcResult createResult = mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.customerId").value(customerId.toString()))
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.quantity").value(6))
            .andReturn();

        ReservationDetailedViewDto created = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            ReservationDetailedViewDto.class);

        Reservation persisted = reservationRepository.findById(created.getId()).orElseThrow();
        assertThat(persisted.getCustomerId()).isEqualTo(customerId);
        assertThat(persisted.getWineId()).isEqualTo(wineId);
        assertThat(persisted.getQuantity()).isEqualTo(6);

        mockMvc.perform(get("/reservations/{id}", created.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(created.getId().toString()));
    }

    @Test
    void updateReservation_persistsChangedQuantityAndReflectsItThroughApi() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(UUID.randomUUID());
        reservation.setQuantity(5);
        reservation.setCreatedAt(LocalDate.now());
        reservation = reservationRepository.saveAndFlush(reservation);

        ReservationDetailedViewDto updateDto = new ReservationDetailedViewDto();
        updateDto.setId(reservation.getId());
        updateDto.setCustomerId(reservation.getCustomerId());
        updateDto.setWineId(reservation.getWineId());
        updateDto.setQuantity(12);
        updateDto.setCreatedAt(reservation.getCreatedAt());

        mockMvc.perform(put("/reservations/{id}", reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(12));

        Reservation refreshed = reservationRepository.findById(reservation.getId()).orElseThrow();
        assertThat(refreshed.getQuantity()).isEqualTo(12);
    }

    @Test
    void deleteReservation_removesEntityAndSubsequentGetReturnsNotFound() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(UUID.randomUUID());
        reservation.setQuantity(3);
        reservation.setCreatedAt(LocalDate.now());
        reservation = reservationRepository.saveAndFlush(reservation);
        UUID reservationId = reservation.getId();

        mockMvc.perform(delete("/reservations/{id}", reservationId))
            .andExpect(status().isNoContent());

        assertThat(reservationRepository.findById(reservationId)).isEmpty();

        mockMvc.perform(get("/reservations/{id}", reservationId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
