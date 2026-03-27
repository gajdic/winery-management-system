package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.mappers.PurchaseRecordMapper;
import cz.muni.fi.pa165.customerservice.service.PurchaseRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PurchaseRecordFacadeTest {

    @Mock
    private PurchaseRecordService purchaseRecordService;

    @Mock
    private PurchaseRecordMapper purchaseRecordMapper;

    @InjectMocks
    private PurchaseRecordFacade purchaseRecordFacade;

    @Test
    void createPurchase_validDto_mapsSavesAndReturnsDto() {
        PurchaseRecordCreateDto createDto = createPurchaseRecordCreateDto();
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        PurchaseRecord savedPurchaseRecord = createPurchaseRecord();
        savedPurchaseRecord.setId(UUID.randomUUID());
        PurchaseRecordDto purchaseRecordDto = createPurchaseRecordDto(
            savedPurchaseRecord.getId(),
            savedPurchaseRecord.getCustomerId(),
            savedPurchaseRecord.getWineId(),
            savedPurchaseRecord.getQuantity(),
            savedPurchaseRecord.getPricePerBottle(),
            savedPurchaseRecord.getPurchaseDate()
        );

        Mockito.when(purchaseRecordMapper.mapToEntity(createDto)).thenReturn(purchaseRecord);
        Mockito.when(purchaseRecordService.save(purchaseRecord)).thenReturn(savedPurchaseRecord);
        Mockito.when(purchaseRecordMapper.mapToDto(savedPurchaseRecord)).thenReturn(purchaseRecordDto);

        PurchaseRecordDto createdPurchase = purchaseRecordFacade.createPurchase(createDto);

        assertThat(createdPurchase).isEqualTo(purchaseRecordDto);
        Mockito.verify(purchaseRecordMapper).mapToEntity(createDto);
        Mockito.verify(purchaseRecordService).save(purchaseRecord);
        Mockito.verify(purchaseRecordMapper).mapToDto(savedPurchaseRecord);
    }

    @Test
    void getPurchase_existingId_returnsMappedDto() {
        UUID purchaseId = UUID.randomUUID();
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setId(purchaseId);
        PurchaseRecordDto purchaseRecordDto = createPurchaseRecordDto(
            purchaseId,
            purchaseRecord.getCustomerId(),
            purchaseRecord.getWineId(),
            purchaseRecord.getQuantity(),
            purchaseRecord.getPricePerBottle(),
            purchaseRecord.getPurchaseDate()
        );

        Mockito.when(purchaseRecordService.findById(purchaseId)).thenReturn(purchaseRecord);
        Mockito.when(purchaseRecordMapper.mapToDto(purchaseRecord)).thenReturn(purchaseRecordDto);

        PurchaseRecordDto foundPurchase = purchaseRecordFacade.getPurchase(purchaseId);

        assertThat(foundPurchase).isEqualTo(purchaseRecordDto);
        Mockito.verify(purchaseRecordService).findById(purchaseId);
        Mockito.verify(purchaseRecordMapper).mapToDto(purchaseRecord);
    }

    @Test
    void getAllPurchases_returnsMappedDtos() {
        PurchaseRecord firstPurchase = createPurchaseRecord();
        firstPurchase.setId(UUID.randomUUID());
        PurchaseRecord secondPurchase = createPurchaseRecord(
            UUID.randomUUID(),
            UUID.randomUUID(),
            3,
            11.50,
            LocalDateTime.of(2026, 3, 20, 9, 15)
        );
        secondPurchase.setId(UUID.randomUUID());

        PurchaseRecordDto firstDto = createPurchaseRecordDto(
            firstPurchase.getId(),
            firstPurchase.getCustomerId(),
            firstPurchase.getWineId(),
            firstPurchase.getQuantity(),
            firstPurchase.getPricePerBottle(),
            firstPurchase.getPurchaseDate()
        );
        PurchaseRecordDto secondDto = createPurchaseRecordDto(
            secondPurchase.getId(),
            secondPurchase.getCustomerId(),
            secondPurchase.getWineId(),
            secondPurchase.getQuantity(),
            secondPurchase.getPricePerBottle(),
            secondPurchase.getPurchaseDate()
        );

        Mockito.when(purchaseRecordService.findAll()).thenReturn(List.of(firstPurchase, secondPurchase));
        Mockito.when(purchaseRecordMapper.mapToDto(firstPurchase)).thenReturn(firstDto);
        Mockito.when(purchaseRecordMapper.mapToDto(secondPurchase)).thenReturn(secondDto);

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getAllPurchases();

        assertThat(purchases).containsExactly(firstDto, secondDto);
        Mockito.verify(purchaseRecordService).findAll();
        Mockito.verify(purchaseRecordMapper).mapToDto(firstPurchase);
        Mockito.verify(purchaseRecordMapper).mapToDto(secondPurchase);
    }

    @Test
    void getAllPurchases_emptyList_returnsEmptyListWithoutMapping() {
        Mockito.when(purchaseRecordService.findAll()).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getAllPurchases();

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findAll();
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByCustomerId_existingCustomerId_returnsMappedDtos() {
        UUID customerId = UUID.randomUUID();
        PurchaseRecord purchaseRecord = createPurchaseRecord(
            customerId,
            UUID.randomUUID(),
            5,
            10.00,
            LocalDateTime.of(2026, 3, 20, 10, 0)
        );
        purchaseRecord.setId(UUID.randomUUID());
        PurchaseRecordDto purchaseRecordDto = createPurchaseRecordDto(
            purchaseRecord.getId(),
            purchaseRecord.getCustomerId(),
            purchaseRecord.getWineId(),
            purchaseRecord.getQuantity(),
            purchaseRecord.getPricePerBottle(),
            purchaseRecord.getPurchaseDate()
        );

        Mockito.when(purchaseRecordService.findByCustomerId(customerId)).thenReturn(List.of(purchaseRecord));
        Mockito.when(purchaseRecordMapper.mapToDto(purchaseRecord)).thenReturn(purchaseRecordDto);

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByCustomerId(customerId);

        assertThat(purchases).containsExactly(purchaseRecordDto);
        Mockito.verify(purchaseRecordService).findByCustomerId(customerId);
        Mockito.verify(purchaseRecordMapper).mapToDto(purchaseRecord);
    }

    @Test
    void getByCustomerId_nonExistingCustomerId_returnsEmptyListWithoutMapping() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordService.findByCustomerId(customerId)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByCustomerId(customerId);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByCustomerId(customerId);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByWineId_existingWineId_returnsMappedDtos() {
        UUID wineId = UUID.randomUUID();
        PurchaseRecord purchaseRecord = createPurchaseRecord(
            UUID.randomUUID(),
            wineId,
            4,
            11.50,
            LocalDateTime.of(2026, 3, 22, 9, 30)
        );
        purchaseRecord.setId(UUID.randomUUID());
        PurchaseRecordDto purchaseRecordDto = createPurchaseRecordDto(
            purchaseRecord.getId(),
            purchaseRecord.getCustomerId(),
            purchaseRecord.getWineId(),
            purchaseRecord.getQuantity(),
            purchaseRecord.getPricePerBottle(),
            purchaseRecord.getPurchaseDate()
        );

        Mockito.when(purchaseRecordService.findByWineId(wineId)).thenReturn(List.of(purchaseRecord));
        Mockito.when(purchaseRecordMapper.mapToDto(purchaseRecord)).thenReturn(purchaseRecordDto);

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByWineId(wineId);

        assertThat(purchases).containsExactly(purchaseRecordDto);
        Mockito.verify(purchaseRecordService).findByWineId(wineId);
        Mockito.verify(purchaseRecordMapper).mapToDto(purchaseRecord);
    }

    @Test
    void getByWineId_nonExistingWineId_returnsEmptyListWithoutMapping() {
        UUID wineId = UUID.randomUUID();
        Mockito.when(purchaseRecordService.findByWineId(wineId)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByWineId(wineId);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByWineId(wineId);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByQuantity_validRange_returnsMappedDtos() {
        PurchaseRecord purchaseRecord = createPurchaseRecord();
        purchaseRecord.setId(UUID.randomUUID());
        PurchaseRecordDto purchaseRecordDto = createPurchaseRecordDto(
            purchaseRecord.getId(),
            purchaseRecord.getCustomerId(),
            purchaseRecord.getWineId(),
            purchaseRecord.getQuantity(),
            purchaseRecord.getPricePerBottle(),
            purchaseRecord.getPurchaseDate()
        );

        Mockito.when(purchaseRecordService.findByQuantity(2, 7)).thenReturn(List.of(purchaseRecord));
        Mockito.when(purchaseRecordMapper.mapToDto(purchaseRecord)).thenReturn(purchaseRecordDto);

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByQuantity(2, 7);

        assertThat(purchases).containsExactly(purchaseRecordDto);
        Mockito.verify(purchaseRecordService).findByQuantity(2, 7);
        Mockito.verify(purchaseRecordMapper).mapToDto(purchaseRecord);
    }

    @Test
    void getByQuantity_emptyResult_returnsEmptyListWithoutMapping() {
        Mockito.when(purchaseRecordService.findByQuantity(2, 7)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByQuantity(2, 7);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByQuantity(2, 7);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByQuantity_nullMin_delegatesToServiceWithNullMin() {
        Mockito.when(purchaseRecordService.findByQuantity(null, 5)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByQuantity(null, 5);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByQuantity(null, 5);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByQuantity_nullMax_delegatesToServiceWithNullMax() {
        Mockito.when(purchaseRecordService.findByQuantity(2, null)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByQuantity(2, null);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByQuantity(2, null);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByQuantity_bothBoundsNull_delegatesToServiceWithNullBounds() {
        Mockito.when(purchaseRecordService.findByQuantity(null, null)).thenReturn(List.of());

        List<PurchaseRecordDto> purchases = purchaseRecordFacade.getByQuantity(null, null);

        assertThat(purchases).isEmpty();
        Mockito.verify(purchaseRecordService).findByQuantity(null, null);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getTotalSpentByCustomer_existingCustomerId_returnsCalculatedValue() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(purchaseRecordService.getTotalSpentByCustomer(customerId)).thenReturn(66.0);

        double totalSpent = purchaseRecordFacade.getTotalSpentByCustomer(customerId);

        assertThat(totalSpent).isEqualTo(66.0);
        Mockito.verify(purchaseRecordService).getTotalSpentByCustomer(customerId);
    }

    @Test
    void createPurchase_serviceThrowsException_propagatesException() {
        PurchaseRecordCreateDto createDto = createPurchaseRecordCreateDto();
        PurchaseRecord purchaseRecord = createPurchaseRecord();

        Mockito.when(purchaseRecordMapper.mapToEntity(createDto)).thenReturn(purchaseRecord);
        Mockito.when(purchaseRecordService.save(purchaseRecord))
            .thenThrow(new IllegalArgumentException("Quantity must be greater than 0"));

        assertThatThrownBy(() -> purchaseRecordFacade.createPurchase(createDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be greater than 0");

        Mockito.verify(purchaseRecordMapper).mapToEntity(createDto);
        Mockito.verify(purchaseRecordService).save(purchaseRecord);
        Mockito.verify(purchaseRecordMapper, Mockito.never()).mapToDto(Mockito.any());
    }

    @Test
    void getPurchase_serviceThrowsException_propagatesException() {
        UUID purchaseId = UUID.randomUUID();
        Mockito.when(purchaseRecordService.findById(purchaseId))
            .thenThrow(new IllegalArgumentException("PurchaseRecord not found: " + purchaseId));

        assertThatThrownBy(() -> purchaseRecordFacade.getPurchase(purchaseId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("PurchaseRecord not found: " + purchaseId);

        Mockito.verify(purchaseRecordService).findById(purchaseId);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void getByQuantity_serviceThrowsException_propagatesException() {
        Mockito.when(purchaseRecordService.findByQuantity(10, 5))
            .thenThrow(new IllegalArgumentException("Min cannot be greater than max"));

        assertThatThrownBy(() -> purchaseRecordFacade.getByQuantity(10, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Min cannot be greater than max");

        Mockito.verify(purchaseRecordService).findByQuantity(10, 5);
        Mockito.verifyNoInteractions(purchaseRecordMapper);
    }

    @Test
    void deletePurchase_callsServiceDeleteById() {
        UUID purchaseId = UUID.randomUUID();

        purchaseRecordFacade.deletePurchase(purchaseId);

        Mockito.verify(purchaseRecordService).deleteById(purchaseId);
    }

    @Test
    void deletePurchase_serviceThrowsException_propagatesException() {
        UUID purchaseId = UUID.randomUUID();
        Mockito.doThrow(new IllegalArgumentException("PurchaseRecord not found: " + purchaseId))
            .when(purchaseRecordService).deleteById(purchaseId);

        assertThatThrownBy(() -> purchaseRecordFacade.deletePurchase(purchaseId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("PurchaseRecord not found: " + purchaseId);

        Mockito.verify(purchaseRecordService).deleteById(purchaseId);
    }

    private PurchaseRecordCreateDto createPurchaseRecordCreateDto() {
        PurchaseRecordCreateDto dto = new PurchaseRecordCreateDto();
        dto.setCustomerId(UUID.randomUUID());
        dto.setWineId(UUID.randomUUID());
        dto.setQuantity(6);
        dto.setPricePerBottle(13.99);
        return dto;
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

    private PurchaseRecordDto createPurchaseRecordDto(
        UUID id,
        UUID customerId,
        UUID wineId,
        int quantity,
        double pricePerBottle,
        LocalDateTime purchaseDate
    ) {
        PurchaseRecordDto dto = new PurchaseRecordDto();
        dto.setId(id);
        dto.setCustomerId(customerId);
        dto.setWineId(wineId);
        dto.setQuantity(quantity);
        dto.setPricePerBottle(pricePerBottle);
        dto.setPurchaseDate(purchaseDate);
        return dto;
    }
}
