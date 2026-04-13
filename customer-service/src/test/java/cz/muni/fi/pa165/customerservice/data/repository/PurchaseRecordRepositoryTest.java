package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PurchaseRecordRepositoryTest {

    @Autowired
    private PurchaseRecordRepository purchaseRecordRepository;

    @Test
    void save_validPurchaseRecord_recordIsPersisted() {
        PurchaseRecord record = createPurchaseRecord();

        PurchaseRecord saved = purchaseRecordRepository.save(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualTo(2);
    }

    @Test
    void findById_existingRecord_recordIsReturned() {
        PurchaseRecord saved = purchaseRecordRepository.save(createPurchaseRecord());

        assertThat(purchaseRecordRepository.findById(saved.getId()))
            .isPresent();
    }

    @Test
    void findAll_multipleRecords_returnsAllRecords() {
        purchaseRecordRepository.save(createPurchaseRecord());
        purchaseRecordRepository.save(createPurchaseRecord());

        List<PurchaseRecord> records = purchaseRecordRepository.findAll();

        assertThat(records).hasSize(2);
    }

    @Test
    void deleteById_existingRecord_recordIsDeleted() {
        PurchaseRecord saved = purchaseRecordRepository.save(createPurchaseRecord());

        purchaseRecordRepository.deleteById(saved.getId());

        assertThat(purchaseRecordRepository.findById(saved.getId()))
            .isEmpty();
    }

    @Test
    void save_nullCustomerId_throwsException() {
        PurchaseRecord record = createPurchaseRecord();
        record.setCustomerId(null);

        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_nullWineId_throwsException() {
        PurchaseRecord record = createPurchaseRecord();
        record.setWineId(null);

        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_nullPurchaseDate_throwsException() {
        PurchaseRecord record = createPurchaseRecord();
        record.setPurchaseDate(null);

        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByCustomerId_existingCustomer_returnsMatchingRecords() {
        UUID customerId = UUID.randomUUID();

        PurchaseRecord r1 = createPurchaseRecord();
        r1.setCustomerId(customerId);

        PurchaseRecord r2 = createPurchaseRecord();
        r2.setCustomerId(customerId);

        PurchaseRecord r3 = createPurchaseRecord();
        r3.setCustomerId(UUID.randomUUID());

        purchaseRecordRepository.save(r1);
        purchaseRecordRepository.save(r2);
        purchaseRecordRepository.save(r3);

        List<PurchaseRecord> result = purchaseRecordRepository.findByCustomerId(customerId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomerId().equals(customerId));
    }

    @Test
    void findByWineId_existingWine_returnsMatchingRecords() {
        UUID wineId = UUID.randomUUID();

        PurchaseRecord r1 = createPurchaseRecord();
        r1.setWineId(wineId);

        PurchaseRecord r2 = createPurchaseRecord();
        r2.setWineId(wineId);

        PurchaseRecord r3 = createPurchaseRecord();
        r3.setWineId(UUID.randomUUID());

        purchaseRecordRepository.save(r1);
        purchaseRecordRepository.save(r2);
        purchaseRecordRepository.save(r3);

        List<PurchaseRecord> result = purchaseRecordRepository.findByWineId(wineId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId));
    }

    @Test
    void findByQuantityBetween_range_returnsMatchingRecords() {
        PurchaseRecord low = createPurchaseRecord();
        low.setQuantity(1);

        PurchaseRecord mid = createPurchaseRecord();
        mid.setQuantity(5);

        PurchaseRecord high = createPurchaseRecord();
        high.setQuantity(10);

        purchaseRecordRepository.save(low);
        purchaseRecordRepository.save(mid);
        purchaseRecordRepository.save(high);

        List<PurchaseRecord> result =
            purchaseRecordRepository.findByQuantityBetween(3, 8);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(5);
    }

    private PurchaseRecord createPurchaseRecord() {
        return new PurchaseRecord(
            null,
            UUID.randomUUID(),
            UUID.randomUUID(),
            2,
            15.5,
            LocalDateTime.now()
        );
    }
}
