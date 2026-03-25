package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.repository.PurchaseRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseRecordService {

    @Autowired
    private final PurchaseRecordRepository purchaseRecordRepository;

    public PurchaseRecordService(PurchaseRecordRepository purchaseRecordRepository) {
        this.purchaseRecordRepository = purchaseRecordRepository;
    }

    public PurchaseRecord save(PurchaseRecord purchaseRecord) {
        purchaseRecord.setPurchaseDate(LocalDateTime.now());
        return purchaseRecordRepository.save(purchaseRecord);
    }

    public PurchaseRecord findById(UUID id) {
        return purchaseRecordRepository.findById(id).orElse(null);
    }

    public List<PurchaseRecord> findAll() {
        return purchaseRecordRepository.findAll();
    }

    public List<PurchaseRecord> findByCustomerId(UUID customerId) {
        return purchaseRecordRepository.findByCustomerId(customerId);
    }

    public List<PurchaseRecord> findByWineId(UUID wineId) {
        return purchaseRecordRepository.findByWineId(wineId);
    }

    public List<PurchaseRecord> findByQuantity(Integer min, Integer max) {
        int lowerBound = (min == null || min < 0) ? 0 : min;
        int upperBound = (max != null) ? max : Integer.MAX_VALUE;

        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }

        return purchaseRecordRepository.findByQuantity(lowerBound, upperBound);
    }

    public double getTotalSpentByCustomer(UUID customerId) {
        return purchaseRecordRepository.findByCustomerId(customerId).stream()
            .mapToDouble(p -> p.getQuantity() * p.getPricePerBottle())
            .sum();
    }

    public void deleteById(UUID id) {
        purchaseRecordRepository.deleteById(id);
    }
}
