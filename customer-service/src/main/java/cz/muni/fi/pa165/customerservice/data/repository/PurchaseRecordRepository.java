package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRecordRepository {

    PurchaseRecord save(PurchaseRecord purchaseRecord);

    Optional<PurchaseRecord> findById(UUID id);

    List<PurchaseRecord> findAll();

    List<PurchaseRecord> findByCustomerId(UUID customerId);

    List<PurchaseRecord> findByWineId(UUID wineId);

    List<PurchaseRecord> findByQuantity(Integer min, Integer max);

    void deleteById(UUID id);
}
