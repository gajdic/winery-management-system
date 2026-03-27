package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.repository.PurchaseRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PurchaseRecordServiceTest {

    @Mock
    private PurchaseRecordRepository purchaseRecordRepository;

    @InjectMocks
    private PurchaseRecordService purchaseRecordService;

    @Test
    void save_validPurchaseRecord_setsPurchaseDateCallsRepositoryAndReturnsSavedPurchaseRecord() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        Mockito.when(purchaseRecordRepository.save(purchaseRecord)).thenReturn(purchaseRecord);

        PurchaseRecord savedPurchaseRecord = purchaseRecordService.save(purchaseRecord);

        assertThat(savedPurchaseRecord).isEqualTo(purchaseRecord);
        assertThat(savedPurchaseRecord.getPurchaseDate()).isNotNull();
        Mockito.verify(purchaseRecordRepository).save(purchaseRecord);
    }

    @Test
    void save_nullPurchaseRecord_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(null));

        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullCustomerId_throwsIllegalArgumentException() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setCustomerId(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullWineId_throwsIllegalArgumentException() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setWineId(null);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_zeroQuantity_throwsIllegalArgumentException() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setQuantity(0);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        assertThat(exception.getMessage()).isEqualTo("Quantity must be greater than 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_negativeQuantity_throwsIllegalArgumentException() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setQuantity(-1);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        assertThat(exception.getMessage()).isEqualTo("Quantity must be greater than 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_negativePricePerBottle_throwsIllegalArgumentException() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setPricePerBottle(-0.01);

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        assertThat(exception.getMessage()).isEqualTo("Price per bottle must be >= 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findById_existingId_returnsPurchaseRecord() {
        UUID purchaseRecordId = UUID.randomUUID();
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setId(purchaseRecordId);
        Mockito.when(purchaseRecordRepository.findById(purchaseRecordId)).thenReturn(Optional.of(purchaseRecord));

        PurchaseRecord foundPurchaseRecord = purchaseRecordService.findById(purchaseRecordId);

        assertThat(foundPurchaseRecord).isEqualTo(purchaseRecord);
        Mockito.verify(purchaseRecordRepository).findById(purchaseRecordId);
    }

    @Test
    void findById_nullId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findById(null));

        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord id cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void findById_nonExistingId_throwsIllegalArgumentException() {
        UUID purchaseRecordId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findById(purchaseRecordId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findById(purchaseRecordId));

        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord not found: " + purchaseRecordId);
        Mockito.verify(purchaseRecordRepository).findById(purchaseRecordId);
    }

    @Test
    void findAll_returnsAllPurchaseRecords() {
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(),
            createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 3, 9.50, LocalDateTime.of(2026, 3, 21, 12, 0))
        );
        Mockito.when(purchaseRecordRepository.findAll()).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findAll();

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findAll();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        Mockito.when(purchaseRecordRepository.findAll()).thenReturn(List.of());

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findAll();

        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findAll();
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingPurchaseRecords() {
        UUID customerId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(customerId, UUID.randomUUID(), 5, 10.00, LocalDateTime.of(2026, 3, 20, 10, 0))
        );
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByCustomerId(customerId);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(List.of());

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByCustomerId(customerId);

        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByCustomerId(null));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByCustomerId(Mockito.any());
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingPurchaseRecords() {
        UUID wineId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(UUID.randomUUID(), wineId, 4, 11.50, LocalDateTime.of(2026, 3, 22, 9, 30))
        );
        Mockito.when(purchaseRecordRepository.findByWineId(wineId)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByWineId(wineId);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        UUID wineId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByWineId(wineId)).thenReturn(List.of());

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByWineId(wineId);

        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nullWineId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByWineId(null));

        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByWineId(Mockito.any());
    }

    @Test
    void findByQuantity_validBounds_callsRepositoryWithProvidedBounds() {
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(2, 7)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, 7);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, 7);
    }

    @Test
    void findByQuantity_nullMin_usesZeroAsLowerBound() {
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, 7)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(null, 7);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, 7);
    }

    @Test
    void findByQuantity_negativeMin_usesZeroAsLowerBound() {
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, 7)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(-3, 7);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, 7);
    }

    @Test
    void findByQuantity_nullMax_usesIntegerMaxValueAsUpperBound() {
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(2, Integer.MAX_VALUE)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, null);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, Integer.MAX_VALUE);
    }

    @Test
    void findByQuantity_bothBoundsNull_usesDefaultBounds() {
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, Integer.MAX_VALUE)).thenReturn(purchaseRecords);

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(null, null);

        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, Integer.MAX_VALUE);
    }

    @Test
    void findByQuantity_validBoundsWithNoMatches_returnsEmptyList() {
        Mockito.when(purchaseRecordRepository.findByQuantity(2, 7)).thenReturn(List.of());

        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, 7);

        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, 7);
    }

    @Test
    void findByQuantity_minGreaterThanMax_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByQuantity(10, 5));

        assertThat(exception.getMessage()).isEqualTo("Min cannot be greater than max");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByQuantity(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void getTotalSpentByCustomer_existingCustomerId_returnsSumOfAllPurchases() {
        UUID customerId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(customerId, UUID.randomUUID(), 2, 10.50, LocalDateTime.of(2026, 3, 20, 8, 0)),
            createPurchaseRecord(customerId, UUID.randomUUID(), 3, 15.00, LocalDateTime.of(2026, 3, 21, 9, 0))
        );
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(purchaseRecords);

        double totalSpent = purchaseRecordService.getTotalSpentByCustomer(customerId);

        assertThat(totalSpent).isEqualTo(66.0);
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void getTotalSpentByCustomer_nonExistingCustomerId_returnsZero() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(List.of());

        double totalSpent = purchaseRecordService.getTotalSpentByCustomer(customerId);

        assertThat(totalSpent).isZero();
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void getTotalSpentByCustomer_nullCustomerId_throwsIllegalArgumentException() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.getTotalSpentByCustomer(null));

        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByCustomerId(Mockito.any());
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        UUID purchaseRecordId = UUID.randomUUID();

        purchaseRecordService.deleteById(purchaseRecordId);

        Mockito.verify(purchaseRecordRepository).deleteById(purchaseRecordId);
    }

    @Test
    void deleteById_nullId_callsRepositoryDeleteByIdWithNull() {
        purchaseRecordService.deleteById(null);

        Mockito.verify(purchaseRecordRepository).deleteById(null);
    }

    @Test
    void deleteById_nonExistingId_stillCallsRepositoryDeleteById() {
        UUID purchaseRecordId = UUID.randomUUID();

        purchaseRecordService.deleteById(purchaseRecordId);

        Mockito.verify(purchaseRecordRepository).deleteById(purchaseRecordId);
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
