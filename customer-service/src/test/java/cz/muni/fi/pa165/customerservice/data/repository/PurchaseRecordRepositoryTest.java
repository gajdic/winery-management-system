package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PurchaseRecordRepositoryTest {

    @Autowired
    private PurchaseRecordRepository purchaseRecordRepository;

    @Test
    void save_ValidPurchaseRecord_RecordIsPersisted() {
        // Arrange
        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();

        // Act
        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(record);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualTo(5);
    }

    @Test
    void findById_ExistingRecord_RecordIsReturned() {
        // Arrange
        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(
            TestDataFactory.purchaseRecordEntity()
        );

        // Act
        var result = purchaseRecordRepository.findById(saved.getId());

        // Assert
        assertThat(result).isPresent();
    }

    @Test
    void findAll_MultipleRecords_ReturnsAllRecords() {
        // Arrange
        purchaseRecordRepository.saveAndFlush(TestDataFactory.purchaseRecordEntity());
        purchaseRecordRepository.saveAndFlush(TestDataFactory.purchaseRecordEntity());

        // Act
        List<PurchaseRecord> result = purchaseRecordRepository.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteById_ExistingRecord_RecordIsDeleted() {
        // Arrange
        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(
            TestDataFactory.purchaseRecordEntity()
        );

        // Act
        purchaseRecordRepository.deleteById(saved.getId());

        // Assert
        assertThat(purchaseRecordRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_NullCustomerId_ThrowsException() {
        // Arrange
        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomerId(null);

        // Act & Assert
        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullWineId_ThrowsException() {
        // Arrange
        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setWineId(null);

        // Act & Assert
        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullPurchaseDate_ThrowsException() {
        // Arrange
        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setPurchaseDate(null);

        // Act & Assert
        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByCustomerId_ExistingCustomer_ReturnsMatchingRecords() {
        // Arrange
        UUID customerId = UUID.randomUUID();

        PurchaseRecord r1 = TestDataFactory.purchaseRecordEntity();
        r1.setCustomerId(customerId);

        PurchaseRecord r2 = TestDataFactory.purchaseRecordEntity();
        r2.setCustomerId(customerId);

        PurchaseRecord r3 = TestDataFactory.purchaseRecordEntity();

        purchaseRecordRepository.saveAll(List.of(r1, r2, r3));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByCustomerId(customerId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getCustomerId().equals(customerId));
    }

    @Test
    void findByWineId_ExistingWine_ReturnsMatchingRecords() {
        // Arrange
        UUID wineId = UUID.randomUUID();

        PurchaseRecord r1 = TestDataFactory.purchaseRecordEntity();
        r1.setWineId(wineId);

        PurchaseRecord r2 = TestDataFactory.purchaseRecordEntity();
        r2.setWineId(wineId);

        PurchaseRecord r3 = TestDataFactory.purchaseRecordEntity();

        purchaseRecordRepository.saveAll(List.of(r1, r2, r3));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByWineId(wineId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getWineId().equals(wineId));
    }

    @Test
    void findByQuantityBetween_Range_ReturnsMatchingRecords() {
        // Arrange
        PurchaseRecord low = TestDataFactory.purchaseRecordEntity();
        low.setQuantity(1);

        PurchaseRecord mid = TestDataFactory.purchaseRecordEntity();
        mid.setQuantity(5);

        PurchaseRecord high = TestDataFactory.purchaseRecordEntity();
        high.setQuantity(10);

        purchaseRecordRepository.saveAll(List.of(low, mid, high));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByQuantityBetween(3, 8);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(5);
    }
}
