package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {

    List<PurchaseRecord> findByCustomer_Id(UUID customerId);

    List<PurchaseRecord> findByWineId(UUID wineId);

    List<PurchaseRecord> findByQuantityBetween(Integer min, Integer max);
}
