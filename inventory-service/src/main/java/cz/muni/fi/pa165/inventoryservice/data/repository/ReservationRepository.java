package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("""
            SELECT r FROM Reservation r
            WHERE (:customerId IS NULL OR r.customerId = :customerId)
              AND (:wineId IS NULL OR r.wineId = :wineId)
              AND (CAST(:createdAfter AS date) IS NULL OR r.createdAt >= :createdAfter)
              AND (CAST(:createdBefore AS date) IS NULL OR r.createdAt <= :createdBefore)
            """)
    List<Reservation> filter(
            @Param("customerId") UUID customerId,
            @Param("wineId") UUID wineId,
            @Param("createdAfter") LocalDate createdAfter,
            @Param("createdBefore") LocalDate createdBefore
    );
}
