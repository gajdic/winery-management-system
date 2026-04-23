package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
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

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save_ValidPurchaseRecord_RecordIsPersisted() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(customer);

        // Act
        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(record);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualTo(5);
        assertThat(saved.getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    void findById_ExistingRecord_RecordIsReturned() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(customer);

        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(record);

        // Act
        var result = purchaseRecordRepository.findById(saved.getId());

        // Assert
        assertThat(result).isPresent();
    }

    @Test
    void findAll_MultipleRecords_ReturnsAllRecords() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord r1 = TestDataFactory.purchaseRecordEntity();
        r1.setCustomer(customer);

        PurchaseRecord r2 = TestDataFactory.purchaseRecordEntity();
        r2.setCustomer(customer);

        purchaseRecordRepository.saveAndFlush(r1);
        purchaseRecordRepository.saveAndFlush(r2);

        // Act
        List<PurchaseRecord> result = purchaseRecordRepository.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteById_ExistingRecord_RecordIsDeleted() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(customer);

        PurchaseRecord saved = purchaseRecordRepository.saveAndFlush(record);

        // Act
        purchaseRecordRepository.deleteById(saved.getId());

        // Assert
        assertThat(purchaseRecordRepository.findById(saved.getId()))
            .isEmpty();
    }

    @Test
    void save_NullCustomer_ThrowsException() {
        // Arrange
        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(null);

        // Act & Assert
        assertThatThrownBy(() -> purchaseRecordRepository.saveAndFlush(record))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullWineId_ThrowsException() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(customer);
        record.setWineId(null);

        // Act & Assert
        assertThatThrownBy(() ->
            purchaseRecordRepository.saveAndFlush(record)
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullPurchaseDate_ThrowsException() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord record = TestDataFactory.purchaseRecordEntity();
        record.setCustomer(customer);
        record.setPurchaseDate(null);

        // Act & Assert
        assertThatThrownBy(() ->
            purchaseRecordRepository.saveAndFlush(record)
        )
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByCustomerId_ExistingCustomer_ReturnsMatchingRecords() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());
        Customer otherCustomer = customerRepository.save(TestDataFactory.customerEntity());

        UUID customerId = customer.getId();

        PurchaseRecord r1 = TestDataFactory.purchaseRecordEntity();
        r1.setCustomer(customer);

        PurchaseRecord r2 = TestDataFactory.purchaseRecordEntity();
        r2.setCustomer(customer);

        PurchaseRecord r3 = TestDataFactory.purchaseRecordEntity();
        r3.setCustomer(otherCustomer);

        purchaseRecordRepository.saveAllAndFlush(List.of(r1, r2, r3));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByCustomer_Id(customerId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result)
            .allMatch(r -> r.getCustomer().getId().equals(customerId));
    }

    @Test
    void findByWineId_ExistingWine_ReturnsMatchingRecords() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());
        Customer otherCustomer = customerRepository.save(TestDataFactory.customerEntity());

        UUID wineId = UUID.randomUUID();

        PurchaseRecord r1 = TestDataFactory.purchaseRecordEntity();
        r1.setCustomer(customer);
        r1.setWineId(wineId);

        PurchaseRecord r2 = TestDataFactory.purchaseRecordEntity();
        r2.setCustomer(customer);
        r2.setWineId(wineId);

        PurchaseRecord r3 = TestDataFactory.purchaseRecordEntity();
        r3.setCustomer(otherCustomer);
        r3.setWineId(UUID.randomUUID());

        purchaseRecordRepository.saveAllAndFlush(List.of(r1, r2, r3));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByWineId(wineId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result)
            .allMatch(r -> r.getWineId().equals(wineId));
    }

    @Test
    void findByQuantityBetween_Range_ReturnsMatchingRecords() {

        // Arrange
        Customer customer = customerRepository.save(TestDataFactory.customerEntity());

        PurchaseRecord low = TestDataFactory.purchaseRecordEntity();
        low.setCustomer(customer);
        low.setQuantity(1);

        PurchaseRecord mid = TestDataFactory.purchaseRecordEntity();
        mid.setCustomer(customer);
        mid.setQuantity(5);

        PurchaseRecord high = TestDataFactory.purchaseRecordEntity();
        high.setCustomer(customer);
        high.setQuantity(10);

        purchaseRecordRepository.saveAllAndFlush(List.of(low, mid, high));

        // Act
        List<PurchaseRecord> result =
            purchaseRecordRepository.findByQuantityBetween(3, 8);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(5);
    }
}
