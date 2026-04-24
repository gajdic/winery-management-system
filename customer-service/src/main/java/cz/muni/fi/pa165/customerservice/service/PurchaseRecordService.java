package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
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
        if (purchaseRecord == null) {
            throw new IllegalArgumentException("PurchaseRecord cannot be null");
        }

        if (purchaseRecord.getCustomer() == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (purchaseRecord.getWineId() == null) {
            throw new IllegalArgumentException("WineId cannot be null");
        }

        if (purchaseRecord.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (purchaseRecord.getPricePerBottle() < 0) {
            throw new IllegalArgumentException("Price per bottle must be >= 0");
        }

        purchaseRecord.setPurchaseDate(LocalDateTime.now());

        return purchaseRecordRepository.save(purchaseRecord);
    }

    public PurchaseRecord findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("PurchaseRecord id cannot be null");
        }

        return purchaseRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PurchaseRecord not found: " + id));
    }

    public List<PurchaseRecord> findAll() {
        return purchaseRecordRepository.findAll();
    }

    public List<PurchaseRecord> findByCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }

        return purchaseRecordRepository.findByCustomer_Id(customerId);
    }

    public List<PurchaseRecord> findByWineId(UUID wineId) {
        if (wineId == null) {
            throw new IllegalArgumentException("WineId cannot be null");
        }

        return purchaseRecordRepository.findByWineId(wineId);
    }

    public List<PurchaseRecord> findByQuantity(Integer min, Integer max) {
        int lowerBound = (min == null || min < 0) ? 0 : min;
        int upperBound = (max != null) ? max : Integer.MAX_VALUE;

        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }

        return purchaseRecordRepository.findByQuantityBetween(lowerBound, upperBound);
    }

    public double getTotalSpentByCustomer(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }

        return purchaseRecordRepository.findByCustomer_Id(customerId).stream()
            .mapToDouble(p -> p.getQuantity() * p.getPricePerBottle())
            .sum();
    }

    public void deleteById(UUID id) {
        purchaseRecordRepository.deleteById(id);
    }
}
