package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.HarvestRecordRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class HarvestRecordServiceTest {

    @Mock
    private HarvestRecordRepository harvestRecordRepository;

    @Mock
    private VineyardRepository vineyardRepository;

    @Mock
    private GrapeTypeRepository grapeTypeRepository;

    @InjectMocks
    private HarvestRecordService harvestRecordService;

    @Test
    void create_validHarvestRecord_returnsSavedRecord() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();

        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(vineyard));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(grapeType));
        Mockito.when(harvestRecordRepository.save(record)).thenReturn(record);

        // Act
        HarvestRecord result = harvestRecordService.create(record);

        // Assert
        assertThat(result).isEqualTo(record);
        Mockito.verify(harvestRecordRepository).save(record);
    }

    @Test
    void create_harvestRecordIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> harvestRecordService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("HarvestRecord cannot be null.");
    }

    @Test
    void create_vineyardIsNull_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setVineyard(null);

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vineyard cannot be null.");
    }

    @Test
    void create_grapeTypeIsNull_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setGrapeType(null);

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type cannot be null.");
    }

    @Test
    void create_vineyardNotFound_throwsResourceNotFoundException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vineyard with id " + TestDataFactory.VINEYARD_ID + " was not found.");
    }

    @Test
    void create_grapeTypeNotFound_throwsResourceNotFoundException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("GrapeType with id " + TestDataFactory.GRAPE_TYPE_ID + " was not found.");
    }

    @Test
    void create_harvestDateIsNull_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setHarvestDate(null);
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Harvest date cannot be null.");
    }

    @Test
    void create_quantityIsZero_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setQuantity(0);
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be greater than 0.");
    }

    @Test
    void create_qualityIsElevenOutOfRange_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setQuality(11);
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quality must be between 1 and 10.");
    }

    @Test
    void create_qualityIsZeroOutOfRange_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setQuality(0);
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.create(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quality must be between 1 and 10.");
    }

    @Test
    void findById_recordFound_returnsRecord() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(harvestRecordRepository.findById(TestDataFactory.HARVEST_RECORD_ID))
                .thenReturn(Optional.of(record));

        // Act
        HarvestRecord result = harvestRecordService.findById(TestDataFactory.HARVEST_RECORD_ID);

        // Assert
        assertThat(result).isEqualTo(record);
    }

    @Test
    void findById_recordNotFound_throwsResourceNotFoundException() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        Mockito.when(harvestRecordRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.findById(unknownId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Harvest record with id " + unknownId + " was not found.");
    }

    @Test
    void findByVineyardId_recordsExist_returnsFilteredList() {
        // Arrange
        List<HarvestRecord> records = List.of(TestDataFactory.harvestRecordEntity());
        Mockito.when(harvestRecordRepository.findByVineyardId(TestDataFactory.VINEYARD_ID))
                .thenReturn(records);

        // Act
        List<HarvestRecord> result = harvestRecordService.findByVineyardId(TestDataFactory.VINEYARD_ID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(records);
    }

    @Test
    void findByGrapeTypeId_recordsExist_returnsFilteredList() {
        // Arrange
        List<HarvestRecord> records = List.of(TestDataFactory.harvestRecordEntity());
        Mockito.when(harvestRecordRepository.findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(records);

        // Act
        List<HarvestRecord> result = harvestRecordService.findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(records);
    }

    @Test
    void findByYear_recordsExist_returnsFilteredList() {
        // Arrange
        List<HarvestRecord> records = List.of(TestDataFactory.harvestRecordEntity());
        Mockito.when(harvestRecordRepository.findByYear(2024)).thenReturn(records);

        // Act
        List<HarvestRecord> result = harvestRecordService.findByYear(2024);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(records);
    }

    @Test
    void findAll_recordsExist_returnsAllRecords() {
        // Arrange
        List<HarvestRecord> records = List.of(TestDataFactory.harvestRecordEntity());
        Mockito.when(harvestRecordRepository.findAll()).thenReturn(records);

        // Act
        List<HarvestRecord> result = harvestRecordService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(records);
    }

    @Test
    void summaryByYear_recordsExist_returnsSumByYear() {
        // Arrange
        HarvestRecord record2024 = TestDataFactory.harvestRecordEntity();
        record2024.setHarvestDate(LocalDate.of(2024, 9, 15));
        record2024.setQuantity(1500.0);

        HarvestRecord record2023 = TestDataFactory.harvestRecordEntity();
        record2023.setHarvestDate(LocalDate.of(2023, 10, 1));
        record2023.setQuantity(1200.0);

        Mockito.when(harvestRecordRepository.findAll()).thenReturn(List.of(record2024, record2023));

        // Act
        Map<String, Double> result = harvestRecordService.summaryByYear();

        // Assert
        assertThat(result).containsEntry("2024", 1500.0);
        assertThat(result).containsEntry("2023", 1200.0);
    }

    @Test
    void summaryByYear_noRecords_returnsEmptyMap() {
        // Arrange
        Mockito.when(harvestRecordRepository.findAll()).thenReturn(List.of());

        // Act
        Map<String, Double> result = harvestRecordService.summaryByYear();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void summaryByVineyard_recordsExist_returnsSumByVineyard() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(harvestRecordRepository.findAll()).thenReturn(List.of(record));

        // Act
        Map<String, Double> result = harvestRecordService.summaryByVineyard();

        // Assert
        assertThat(result).containsEntry(TestDataFactory.VINEYARD_ID.toString(), 1500.0);
    }

    @Test
    void update_validHarvestRecord_returnsUpdatedRecord() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));
        Mockito.when(harvestRecordRepository.update(record)).thenReturn(record);

        // Act
        HarvestRecord result = harvestRecordService.update(record);

        // Assert
        assertThat(result).isEqualTo(record);
        Mockito.verify(harvestRecordRepository).update(record);
    }

    @Test
    void update_harvestRecordIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> harvestRecordService.update(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Harvest record cannot be null.");
    }

    @Test
    void update_idIsNull_throwsIllegalArgumentException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        record.setId(null);

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.update(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Harvest record id cannot be null.");
    }

    @Test
    void update_harvestRecordNotFound_throwsResourceNotFoundException() {
        // Arrange
        HarvestRecord record = TestDataFactory.harvestRecordEntity();
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(TestDataFactory.grapeTypeEntity()));
        Mockito.when(harvestRecordRepository.update(record))
                .thenThrow(new IllegalArgumentException("Record not found"));

        // Act & Assert
        assertThatThrownBy(() -> harvestRecordService.update(record))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Harvest record with id " + record.getId() + " was not found.");
    }

    @Test
    void deleteById_existingRecord_callsRepository() {
        // Act
        harvestRecordService.deleteById(TestDataFactory.HARVEST_RECORD_ID);

        // Assert
        Mockito.verify(harvestRecordRepository).deleteById(TestDataFactory.HARVEST_RECORD_ID);
    }
}
