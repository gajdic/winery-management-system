package cz.muni.fi.pa165.customerservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
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
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        Mockito.when(purchaseRecordRepository.save(purchaseRecord)).thenReturn(purchaseRecord);

        // Act
        PurchaseRecord savedPurchaseRecord = purchaseRecordService.save(purchaseRecord);

        // Assert
        assertThat(savedPurchaseRecord).isEqualTo(purchaseRecord);
        assertThat(savedPurchaseRecord.getPurchaseDate()).isNotNull();
        Mockito.verify(purchaseRecordRepository).save(purchaseRecord);
    }

    @Test
    void save_nullPurchaseRecord_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullCustomerId_throwsIllegalArgumentException() {
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setCustomerId(null);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_nullWineId_throwsIllegalArgumentException() {
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setWineId(null);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_zeroQuantity_throwsIllegalArgumentException() {
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setQuantity(0);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Quantity must be greater than 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_negativeQuantity_throwsIllegalArgumentException() {
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setQuantity(-1);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Quantity must be greater than 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save_negativePricePerBottle_throwsIllegalArgumentException() {
        // Arrange
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setPricePerBottle(-0.01);

        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.save(purchaseRecord));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Price per bottle must be >= 0");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findById_existingId_returnsPurchaseRecord() {
        // Arrange
        UUID purchaseRecordId = UUID.randomUUID();
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setId(purchaseRecordId);
        Mockito.when(purchaseRecordRepository.findById(purchaseRecordId)).thenReturn(Optional.of(purchaseRecord));

        // Act
        PurchaseRecord foundPurchaseRecord = purchaseRecordService.findById(purchaseRecordId);

        // Assert
        assertThat(foundPurchaseRecord).isEqualTo(purchaseRecord);
        Mockito.verify(purchaseRecordRepository).findById(purchaseRecordId);
    }

    @Test
    void findById_nullId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findById(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord id cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void findById_nonExistingId_throwsResourceNotFoundException() {
        // Arrange
        UUID purchaseRecordId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findById(purchaseRecordId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> purchaseRecordService.findById(purchaseRecordId));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("PurchaseRecord not found: " + purchaseRecordId);
        Mockito.verify(purchaseRecordRepository).findById(purchaseRecordId);
    }

    @Test
    void findAll_returnsAllPurchaseRecords() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(),
            createPurchaseRecord(UUID.randomUUID(), UUID.randomUUID(), 3, 9.50, LocalDateTime.of(2026, 3, 21, 12, 0))
        );
        Mockito.when(purchaseRecordRepository.findAll()).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findAll();

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findAll();
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // Arrange
        Mockito.when(purchaseRecordRepository.findAll()).thenReturn(List.of());

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findAll();

        // Assert
        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findAll();
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsMatchingPurchaseRecords() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(customerId, UUID.randomUUID(), 5, 10.00, LocalDateTime.of(2026, 3, 20, 10, 0))
        );
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByCustomerId(customerId);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmptyList() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(List.of());

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByCustomerId(customerId);

        // Assert
        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void findByCustomerId_nullCustomerId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByCustomerId(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByCustomerId(Mockito.any());
    }

    @Test
    void findByWineId_existingWineId_returnsMatchingPurchaseRecords() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(UUID.randomUUID(), wineId, 4, 11.50, LocalDateTime.of(2026, 3, 22, 9, 30))
        );
        Mockito.when(purchaseRecordRepository.findByWineId(wineId)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByWineId(wineId);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nonExistingWineId_returnsEmptyList() {
        // Arrange
        UUID wineId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByWineId(wineId)).thenReturn(List.of());

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByWineId(wineId);

        // Assert
        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByWineId(wineId);
    }

    @Test
    void findByWineId_nullWineId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByWineId(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("WineId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByWineId(Mockito.any());
    }

    @Test
    void findByQuantity_validBounds_callsRepositoryWithProvidedBounds() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(2, 7)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, 7);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, 7);
    }

    @Test
    void findByQuantity_nullMin_usesZeroAsLowerBound() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, 7)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(null, 7);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, 7);
    }

    @Test
    void findByQuantity_negativeMin_usesZeroAsLowerBound() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, 7)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(-3, 7);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, 7);
    }

    @Test
    void findByQuantity_nullMax_usesIntegerMaxValueAsUpperBound() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(2, Integer.MAX_VALUE)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, null);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, Integer.MAX_VALUE);
    }

    @Test
    void findByQuantity_bothBoundsNull_usesDefaultBounds() {
        // Arrange
        List<PurchaseRecord> purchaseRecords = List.of(createPurchaseRecord());
        Mockito.when(purchaseRecordRepository.findByQuantity(0, Integer.MAX_VALUE)).thenReturn(purchaseRecords);

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(null, null);

        // Assert
        assertThat(foundPurchaseRecords).isEqualTo(purchaseRecords);
        Mockito.verify(purchaseRecordRepository).findByQuantity(0, Integer.MAX_VALUE);
    }

    @Test
    void findByQuantity_validBoundsWithNoMatches_returnsEmptyList() {
        // Arrange
        Mockito.when(purchaseRecordRepository.findByQuantity(2, 7)).thenReturn(List.of());

        // Act
        List<PurchaseRecord> foundPurchaseRecords = purchaseRecordService.findByQuantity(2, 7);

        // Assert
        assertThat(foundPurchaseRecords).isEmpty();
        Mockito.verify(purchaseRecordRepository).findByQuantity(2, 7);
    }

    @Test
    void findByQuantity_minGreaterThanMax_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.findByQuantity(10, 5));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Min cannot be greater than max");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByQuantity(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void getTotalSpentByCustomer_existingCustomerId_returnsSumOfAllPurchases() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        List<PurchaseRecord> purchaseRecords = List.of(
            createPurchaseRecord(customerId, UUID.randomUUID(), 2, 10.50, LocalDateTime.of(2026, 3, 20, 8, 0)),
            createPurchaseRecord(customerId, UUID.randomUUID(), 3, 15.00, LocalDateTime.of(2026, 3, 21, 9, 0))
        );
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(purchaseRecords);

        // Act
        double totalSpent = purchaseRecordService.getTotalSpentByCustomer(customerId);

        // Assert
        assertThat(totalSpent).isEqualTo(66.0);
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void getTotalSpentByCustomer_nonExistingCustomerId_returnsZero() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordRepository.findByCustomerId(customerId)).thenReturn(List.of());

        // Act
        double totalSpent = purchaseRecordService.getTotalSpentByCustomer(customerId);

        // Assert
        assertThat(totalSpent).isZero();
        Mockito.verify(purchaseRecordRepository).findByCustomerId(customerId);
    }

    @Test
    void getTotalSpentByCustomer_nullCustomerId_throwsIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception =
            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> purchaseRecordService.getTotalSpentByCustomer(null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("CustomerId cannot be null");
        Mockito.verify(purchaseRecordRepository, Mockito.never()).findByCustomerId(Mockito.any());
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        // Arrange
        UUID purchaseRecordId = UUID.randomUUID();

        // Act
        purchaseRecordService.deleteById(purchaseRecordId);

        // Assert
        Mockito.verify(purchaseRecordRepository).deleteById(purchaseRecordId);
    }

    @Test
    void deleteById_nullId_callsRepositoryDeleteByIdWithNull() {
        // Arrange + Act
        purchaseRecordService.deleteById(null);

        // Assert
        Mockito.verify(purchaseRecordRepository).deleteById(null);
    }

    @Test
    void deleteById_nonExistingId_stillCallsRepositoryDeleteById() {
        // Arrange
        UUID purchaseRecordId = UUID.randomUUID();

        // Act
        purchaseRecordService.deleteById(purchaseRecordId);

        // Assert
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
