package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPurchaseRecordRepository implements PurchaseRecordRepository {

    private final Map<UUID, PurchaseRecord> storage = new HashMap<>();


    @Override
    public PurchaseRecord save(PurchaseRecord purchaseRecord) {
        if (purchaseRecord == null) {
            throw new IllegalArgumentException("PurchaseRecord cannot be null");
        }

        if (purchaseRecord.getId() == null) {
            purchaseRecord.setId(UUID.randomUUID());
        }

        storage.put(purchaseRecord.getId(), purchaseRecord);
        return purchaseRecord;
    }

    @Override
    public Optional<PurchaseRecord> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<PurchaseRecord> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<PurchaseRecord> findByCustomerId(UUID customerId) {
        return storage.values().stream()
            .filter(p -> customerId.equals(p.getCustomerId()))
            .toList();
    }

    @Override
    public List<PurchaseRecord> findByWineId(UUID wineId) {
        return storage.values().stream()
            .filter(p -> wineId.equals(p.getWineId()))
            .toList();
    }

    @Override
    public List<PurchaseRecord> findByQuantity(Integer min, Integer max) {
        return storage.values().stream()
            .filter(p -> p.getQuantity() >= min && p.getQuantity() <= max)
            .toList();
    }


    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
