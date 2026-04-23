package cz.muni.fi.pa165.inventoryservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.inventoryservice.InventoryServiceApplication;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private StockRepository stockRepository;

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
        createDto.setStatus(ReservationStatus.PENDING);
        createDto.setCreatedAt(LocalDate.of(2025, 10, 15));

        MvcResult createResult = mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.customerId").value(customerId.toString()))
            .andExpect(jsonPath("$.wineId").value(wineId.toString()))
            .andExpect(jsonPath("$.quantity").value(6))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andReturn();

        ReservationDetailedViewDto created = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            ReservationDetailedViewDto.class);

        Reservation persisted = reservationRepository.findById(created.getId()).orElseThrow();
        assertThat(persisted.getCustomerId()).isEqualTo(customerId);
        assertThat(persisted.getWineId()).isEqualTo(wineId);
        assertThat(persisted.getQuantity()).isEqualTo(6);
        assertThat(persisted.getStatus()).isEqualTo(ReservationStatus.PENDING);

        mockMvc.perform(get("/reservations/{id}", created.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(created.getId().toString()));
    }

    @Test
    void updateStatus_toCompleted_decreasesStockQuantityAndReservedQuantity() throws Exception {
        UUID wineId = UUID.randomUUID();

        Stock stock = new Stock();
        stock.setWineId(wineId);
        stock.setQuantity(100);
        stock.setReservedQuantity(10);
        stockRepository.saveAndFlush(stock);

        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(wineId);
        reservation.setQuantity(10);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDate.now());
        reservation = reservationRepository.saveAndFlush(reservation);

        mockMvc.perform(patch("/reservations/{id}/status", reservation.getId())
                .param("status", ReservationStatus.COMPLETED.name()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));

        Stock refreshed = stockRepository.findByWineId(wineId).orElseThrow();
        assertThat(refreshed.getQuantity()).isEqualTo(90);
        assertThat(refreshed.getReservedQuantity()).isEqualTo(0);
    }

    @Test
    void updateStatus_toCanceled_releasesReservedQuantityOnly() throws Exception {
        UUID wineId = UUID.randomUUID();

        Stock stock = new Stock();
        stock.setWineId(wineId);
        stock.setQuantity(100);
        stock.setReservedQuantity(10);
        stockRepository.saveAndFlush(stock);

        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(wineId);
        reservation.setQuantity(10);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDate.now());
        reservation = reservationRepository.saveAndFlush(reservation);

        mockMvc.perform(patch("/reservations/{id}/status", reservation.getId())
                .param("status", ReservationStatus.CANCELED.name()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELED"));

        Stock refreshed = stockRepository.findByWineId(wineId).orElseThrow();
        assertThat(refreshed.getQuantity()).isEqualTo(100);
        assertThat(refreshed.getReservedQuantity()).isEqualTo(0);
    }
}
