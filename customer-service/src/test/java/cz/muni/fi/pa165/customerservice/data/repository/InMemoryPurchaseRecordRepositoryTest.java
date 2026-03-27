package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryPurchaseRecordRepositoryTest {

    private InMemoryPurchaseRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPurchaseRecordRepository();
    }

    @Test
    void save_purchaseRecordWithoutId_assignsIdAndStoresPurchaseRecord() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();

        PurchaseRecord savedPurchaseRecord = repository.save(purchaseRecord);

        assertThat(savedPurchaseRecord.getId()).isNotNull();
        assertThat(repository.findById(savedPurchaseRecord.getId())).contains(savedPurchaseRecord);
        assertThat(repository.findAll()).containsExactly(savedPurchaseRecord);
    }

    @Test
    void save_nullPurchaseRecord_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> repository.save(null));

        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord cannot be null");
    }

    @Test
    void save_purchaseRecordWithExistingId_updatesStoredPurchaseRecord() {
        UUID purchaseRecordId = UUID.randomUUID();
        PurchaseRecord originalPurchaseRecord = createPurchaseRecord();
        originalPurchaseRecord.setId(purchaseRecordId);
        repository.save(originalPurchaseRecord);

        PurchaseRecord updatedPurchaseRecord = createPurchaseRecord();
        updatedPurchaseRecord.setId(purchaseRecordId);
        updatedPurchaseRecord.setQuantity(20);
        updatedPurchaseRecord.setPricePerBottle(19.99);

        PurchaseRecord savedPurchaseRecord = repository.save(updatedPurchaseRecord);

        assertThat(savedPurchaseRecord.getId()).isEqualTo(purchaseRecordId);
        assertThat(repository.findAll()).containsExactly(updatedPurchaseRecord);
        assertThat(repository.findById(purchaseRecordId)).contains(updatedPurchaseRecord);
    }

    @Test
    void save_purchaseRecordWithExistingId_preservesOnlyLatestValue() {
        UUID purchaseRecordId = UUID.randomUUID();
        PurchaseRecord originalPurchaseRecord = createPurchaseRecord();
        originalPurchaseRecord.setId(purchaseRecordId);
        repository.save(originalPurchaseRecord);

        PurchaseRecord updatedPurchaseRecord = createPurchaseRecord();
        updatedPurchaseRecord.setId(purchaseRecordId);
        updatedPurchaseRecord.setQuantity(99);

        repository.save(updatedPurchaseRecord);

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(purchaseRecordId)).contains(updatedPurchaseRecord);
    }

    @Test
    void findById_existingId_returnsPurchaseRecord() {
        PurchaseRecord savedPurchaseRecord = repository.save(createPurchaseRecord());

        var foundPurchaseRecord = repository.findById(savedPurchaseRecord.getId());

        assertThat(foundPurchaseRecord).contains(savedPurchaseRecord);
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        var foundPurchaseRecord = repository.findById(UUID.randomUUID());

        assertThat(foundPurchaseRecord).isEmpty();
    }

    @Test
    void findById_nullId_returnsEmptyOptional() {
        var foundPurchaseRecord = repository.findById(null);

        assertThat(foundPurchaseRecord).isEmpty();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findAll_multiplePurchaseRecords_returnsAllPurchaseRecords() {
        PurchaseRecord firstPurchaseRecord = createPurchaseRecord();
        PurchaseRecord secondPurchaseRecord = createPurchaseRecord(
            UUID.randomUUID(),
            UUID.randomUUID(),
            3,
            11.50,
            LocalDateTime.of(2026, 3, 20, 9, 15)
        );

        PurchaseRecord savedFirst = repository.save(firstPurchaseRecord);
        PurchaseRecord savedSecond = repository.save(secondPurchaseRecord);

        assertThat(repository.findAll())
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingPurchaseRecords() {
        UUID customerId = UUID.randomUUID();
        PurchaseRecord matchingFirst = createPurchaseRecord(customerId, UUID.randomUUID(), 5, 10.00, LocalDateTime.now());
        PurchaseRecord matchingSecond = createPurchaseRecord(customerId, UUID.randomUUID(), 8, 15.00, LocalDateTime.now().minusDays(1));
        PurchaseRecord nonMatching = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 2, 7.50, LocalDateTime.now().minusDays(2));

        PurchaseRecord savedFirst = repository.save(matchingFirst);
        PurchaseRecord savedSecond = repository.save(matchingSecond);
        repository.save(nonMatching);

        assertThat(repository.findByCustomerId(customerId))
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        repository.save(createPurchaseRecord());

        assertThat(repository.findByCustomerId(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsNullPointerException() {
        repository.save(createPurchaseRecord());

        assertThrows(NullPointerException.class, () -> repository.findByCustomerId(null));
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingPurchaseRecords() {
        UUID wineId = UUID.randomUUID();
        PurchaseRecord matchingFirst = createPurchaseRecord(UUID.randomUUID(), wineId, 5, 10.00, LocalDateTime.now());
        PurchaseRecord matchingSecond = createPurchaseRecord(UUID.randomUUID(), wineId, 8, 15.00, LocalDateTime.now().minusDays(1));
        PurchaseRecord nonMatching = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 2, 7.50, LocalDateTime.now().minusDays(2));

        PurchaseRecord savedFirst = repository.save(matchingFirst);
        PurchaseRecord savedSecond = repository.save(matchingSecond);
        repository.save(nonMatching);

        assertThat(repository.findByWineId(wineId))
            .hasSize(2)
            .containsExactlyInAnyOrder(savedFirst, savedSecond);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        repository.save(createPurchaseRecord());

        assertThat(repository.findByWineId(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByWineId_nullWineId_throwsNullPointerException() {
        repository.save(createPurchaseRecord());

        assertThrows(NullPointerException.class, () -> repository.findByWineId(null));
    }

    @Test
    void findByQuantity_matchingRange_returnsMatchingPurchaseRecords() {
        PurchaseRecord belowRange = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 1, 6.50, LocalDateTime.now());
        PurchaseRecord atLowerBound = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 3, 8.00, LocalDateTime.now().minusDays(1));
        PurchaseRecord withinRange = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 5, 9.50, LocalDateTime.now().minusDays(2));
        PurchaseRecord atUpperBound = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 7, 12.00, LocalDateTime.now().minusDays(3));
        PurchaseRecord aboveRange = createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 9, 14.00, LocalDateTime.now().minusDays(4));

        repository.save(belowRange);
        PurchaseRecord savedLowerBound = repository.save(atLowerBound);
        PurchaseRecord savedWithinRange = repository.save(withinRange);
        PurchaseRecord savedUpperBound = repository.save(atUpperBound);
        repository.save(aboveRange);

        assertThat(repository.findByQuantity(3, 7))
            .hasSize(3)
            .containsExactlyInAnyOrder(savedLowerBound, savedWithinRange, savedUpperBound);
    }

    @Test
    void findByQuantity_nonMatchingRange_returnsEmptyList() {
        repository.save(createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 2, 7.50, LocalDateTime.now()));

        assertThat(repository.findByQuantity(5, 10)).isEmpty();
    }

    @Test
    void findByQuantity_nullMin_throwsNullPointerException() {
        repository.save(createPurchaseRecord());

        assertThrows(NullPointerException.class, () -> repository.findByQuantity(null, 5));
    }

    @Test
    void findByQuantity_nullMax_throwsNullPointerException() {
        repository.save(createPurchaseRecord());

        assertThrows(NullPointerException.class, () -> repository.findByQuantity(1, null));
    }

    @Test
    void deleteById_existingId_removesPurchaseRecord() {
        PurchaseRecord savedPurchaseRecord = repository.save(createPurchaseRecord());

        repository.deleteById(savedPurchaseRecord.getId());

        assertThat(repository.findById(savedPurchaseRecord.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingId_doesNothing() {
        PurchaseRecord savedPurchaseRecord = repository.save(createPurchaseRecord());

        repository.deleteById(UUID.randomUUID());

        assertThat(repository.findAll()).containsExactly(savedPurchaseRecord);
    }

    private PurchaseRecord createPurchaseRecord() {
        return createPurchaseRecord(
            UUID.randomUUID(),
            UUID.randomUUID(),
            6,
            13.99,
            LocalDateTime.of(2026, 3, 27, 10, 30)
        );
    }

    private PurchaseRecord createPurchaseRecord(
        UUID customerId,
        UUID wineId,
        int quantity,
        double pricePerBottle,
        LocalDateTime purchaseDate
    ) {
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setCustomerId(customerId);
        purchaseRecord.setWineId(wineId);
        purchaseRecord.setQuantity(quantity);
        purchaseRecord.setPricePerBottle(pricePerBottle);
        purchaseRecord.setPurchaseDate(purchaseDate);
        return purchaseRecord;
    }
}
