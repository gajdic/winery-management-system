package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private UUID customerId1;
    private UUID customerId2;
    private UUID wineId1;

    @BeforeEach
    void initData() {
        customerId1 = UUID.randomUUID();
        customerId2 = UUID.randomUUID();
        wineId1 = UUID.randomUUID();
        UUID wineId2 = UUID.randomUUID();

        Reservation r1 = new Reservation();
        r1.setCustomerId(customerId1);
        r1.setWineId(wineId1);
        r1.setQuantity(10);
        r1.setStatus(ReservationStatus.PENDING);
        r1.setCreatedAt(LocalDate.of(2026, 4, 1));
        testEntityManager.persist(r1);

        Reservation r2 = new Reservation();
        r2.setCustomerId(customerId1);
        r2.setWineId(wineId2);
        r2.setQuantity(5);
        r2.setStatus(ReservationStatus.COMPLETED);
        r2.setCreatedAt(LocalDate.of(2026, 4, 5));
        testEntityManager.persist(r2);

        Reservation r3 = new Reservation();
        r3.setCustomerId(customerId2);
        r3.setWineId(wineId1);
        r3.setQuantity(8);
        r3.setStatus(ReservationStatus.CANCELED);
        r3.setCreatedAt(LocalDate.of(2026, 4, 10));
        testEntityManager.persist(r3);

        testEntityManager.flush();
    }

    // --- filter ---

    @Test
    void filter_allNullParams_returnsAllReservations() {
        List<Reservation> result = reservationRepository.filter(null, null, null, null);

        assertThat(result).hasSize(3);
    }

    @Test
    void filter_byCustomerId_returnsMatchingReservations() {
        List<Reservation> result = reservationRepository.filter(customerId1, null, null, null);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomerId().equals(customerId1));
    }

    @Test
    void filter_byWineId_returnsMatchingReservations() {
        List<Reservation> result = reservationRepository.filter(null, wineId1, null, null);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId1));
    }

    @Test
    void filter_byCreatedAfter_returnsReservationsAfterDate() {
        List<Reservation> result = reservationRepository.filter(
                null, null, LocalDate.of(2026, 4, 3), null);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> !r.getCreatedAt().isBefore(LocalDate.of(2026, 4, 3)));
    }

    @Test
    void filter_byCreatedBefore_returnsReservationsBeforeDate() {
        List<Reservation> result = reservationRepository.filter(
                null, null, null, LocalDate.of(2026, 4, 6));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> !r.getCreatedAt().isAfter(LocalDate.of(2026, 4, 6)));
    }

    @Test
    void filter_byDateRange_returnsReservationsInRange() {
        List<Reservation> result = reservationRepository.filter(
                null, null, LocalDate.of(2026, 4, 4), LocalDate.of(2026, 4, 8));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    void filter_byCustomerIdAndWineId_returnsMatchingReservation() {
        List<Reservation> result = reservationRepository.filter(customerId1, wineId1, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(10);
        assertThat(result.get(0).getStatus()).isEqualTo(ReservationStatus.PENDING);
    }

    @Test
    void filter_allParams_returnsExactMatch() {
        List<Reservation> result = reservationRepository.filter(
                customerId2, wineId1, LocalDate.of(2026, 4, 9), LocalDate.of(2026, 4, 11));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(customerId2);
        assertThat(result.get(0).getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    void filter_noMatch_returnsEmptyList() {
        List<Reservation> result = reservationRepository.filter(
                UUID.randomUUID(), null, null, null);

        assertThat(result).isEmpty();
    }

    // --- CRUD operations ---

    @Test
    void save_newReservation_persistsCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(UUID.randomUUID());
        reservation.setWineId(UUID.randomUUID());
        reservation.setQuantity(15);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDate.of(2026, 4, 12));

        Reservation saved = reservationRepository.save(reservation);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualTo(15);
        assertThat(saved.getStatus()).isEqualTo(ReservationStatus.PENDING);
    }

    @Test
    void findAll_returnsAllReservations() {
        List<Reservation> all = reservationRepository.findAll();

        assertThat(all).hasSize(3);
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<Reservation> found = reservationRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }
}
