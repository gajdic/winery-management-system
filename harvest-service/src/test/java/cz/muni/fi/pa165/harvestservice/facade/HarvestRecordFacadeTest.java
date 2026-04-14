package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordViewDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.mappers.HarvestRecordMapper;
import cz.muni.fi.pa165.harvestservice.service.HarvestRecordService;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HarvestRecordFacadeTest {

    @Mock
    private HarvestRecordService harvestRecordService;

    @Mock
    private HarvestRecordMapper harvestRecordMapper;

    @InjectMocks
    private HarvestRecordFacade harvestRecordFacade;

    @Test
    void create_validDto_mapsAndDelegatesToService() {
        // Arrange
        HarvestRecordCreateDto createDto = TestDataFactory.harvestRecordCreateDto();
        HarvestRecord entity = TestDataFactory.harvestRecordEntity();
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();

        Mockito.when(harvestRecordMapper.mapToEntity(createDto)).thenReturn(entity);
        Mockito.when(harvestRecordService.create(entity)).thenReturn(entity);
        Mockito.when(harvestRecordMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        HarvestRecordViewDto result = harvestRecordFacade.create(createDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(harvestRecordMapper).mapToEntity(createDto);
        Mockito.verify(harvestRecordService).create(entity);
        Mockito.verify(harvestRecordMapper).mapToDto(entity);
    }

    @Test
    void findById_existingId_returnsViewDto() {
        // Arrange
        HarvestRecord entity = TestDataFactory.harvestRecordEntity();
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();

        Mockito.when(harvestRecordService.findById(TestDataFactory.HARVEST_RECORD_ID)).thenReturn(entity);
        Mockito.when(harvestRecordMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        HarvestRecordViewDto result = harvestRecordFacade.findById(TestDataFactory.HARVEST_RECORD_ID);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(harvestRecordService).findById(TestDataFactory.HARVEST_RECORD_ID);
    }

    @Test
    void findByVineyardId_recordsExist_returnsMappedList() {
        // Arrange
        List<HarvestRecord> entities = List.of(TestDataFactory.harvestRecordEntity());
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());

        Mockito.when(harvestRecordService.findByVineyardId(TestDataFactory.VINEYARD_ID)).thenReturn(entities);
        Mockito.when(harvestRecordMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<HarvestRecordViewDto> result = harvestRecordFacade.findByVineyardId(TestDataFactory.VINEYARD_ID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(harvestRecordService).findByVineyardId(TestDataFactory.VINEYARD_ID);
    }

    @Test
    void findByGrapeTypeId_recordsExist_returnsMappedList() {
        // Arrange
        List<HarvestRecord> entities = List.of(TestDataFactory.harvestRecordEntity());
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());

        Mockito.when(harvestRecordService.findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID)).thenReturn(entities);
        Mockito.when(harvestRecordMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<HarvestRecordViewDto> result = harvestRecordFacade.findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(harvestRecordService).findByGrapeTypeId(TestDataFactory.GRAPE_TYPE_ID);
    }

    @Test
    void findByYear_recordsExist_returnsMappedList() {
        // Arrange
        List<HarvestRecord> entities = List.of(TestDataFactory.harvestRecordEntity());
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());

        Mockito.when(harvestRecordService.findByYear(2024)).thenReturn(entities);
        Mockito.when(harvestRecordMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<HarvestRecordViewDto> result = harvestRecordFacade.findByYear(2024);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(harvestRecordService).findByYear(2024);
    }

    @Test
    void findAll_recordsExist_returnsMappedList() {
        // Arrange
        List<HarvestRecord> entities = List.of(TestDataFactory.harvestRecordEntity());
        List<HarvestRecordViewDto> viewDtos = List.of(TestDataFactory.harvestRecordViewDto());

        Mockito.when(harvestRecordService.findAll()).thenReturn(entities);
        Mockito.when(harvestRecordMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<HarvestRecordViewDto> result = harvestRecordFacade.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(harvestRecordService).findAll();
    }

    @Test
    void summaryByYear_recordsExist_returnsMappedSummary() {
        // Arrange
        Map<String, Double> summaryMap = Map.of("2024", 1500.0);
        List<HarvestSummaryDto> summaryDtos = List.of(new HarvestSummaryDto("2024", 1500.0));

        Mockito.when(harvestRecordService.summaryByYear()).thenReturn(summaryMap);
        Mockito.when(harvestRecordMapper.mapSummaryToDtoList(summaryMap)).thenReturn(summaryDtos);

        // Act
        List<HarvestSummaryDto> result = harvestRecordFacade.summaryByYear();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGroupByValue()).isEqualTo("2024");
        assertThat(result.get(0).getTotalQuantity()).isEqualTo(1500.0);
        Mockito.verify(harvestRecordService).summaryByYear();
    }

    @Test
    void summaryByVineyard_recordsExist_returnsMappedSummary() {
        // Arrange
        Map<String, Double> summaryMap = Map.of(TestDataFactory.VINEYARD_ID.toString(), 1500.0);
        List<HarvestSummaryDto> summaryDtos = List.of(
                new HarvestSummaryDto(TestDataFactory.VINEYARD_ID.toString(), 1500.0));

        Mockito.when(harvestRecordService.summaryByVineyard()).thenReturn(summaryMap);
        Mockito.when(harvestRecordMapper.mapSummaryToDtoList(summaryMap)).thenReturn(summaryDtos);

        // Act
        List<HarvestSummaryDto> result = harvestRecordFacade.summaryByVineyard();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTotalQuantity()).isEqualTo(1500.0);
        Mockito.verify(harvestRecordService).summaryByVineyard();
    }

    @Test
    void update_validIdAndDto_mapsAndDelegatesToService() {
        // Arrange
        HarvestRecordUpdateDto updateDto = new HarvestRecordUpdateDto(
                TestDataFactory.GRAPE_TYPE_ID, TestDataFactory.VINEYARD_ID,
                LocalDate.of(2024, 9, 15), 1500.0, 8);
        HarvestRecord entity = TestDataFactory.harvestRecordEntity();
        HarvestRecordViewDto viewDto = TestDataFactory.harvestRecordViewDto();

        Mockito.when(harvestRecordMapper.mapToEntity(TestDataFactory.HARVEST_RECORD_ID, updateDto)).thenReturn(entity);
        Mockito.when(harvestRecordService.update(entity)).thenReturn(entity);
        Mockito.when(harvestRecordMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        HarvestRecordViewDto result = harvestRecordFacade.update(TestDataFactory.HARVEST_RECORD_ID, updateDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(harvestRecordMapper).mapToEntity(TestDataFactory.HARVEST_RECORD_ID, updateDto);
        Mockito.verify(harvestRecordService).update(entity);
    }

    @Test
    void deleteById_existingId_delegatesToService() {
        // Act
        harvestRecordFacade.deleteById(TestDataFactory.HARVEST_RECORD_ID);

        // Assert
        Mockito.verify(harvestRecordService).deleteById(TestDataFactory.HARVEST_RECORD_ID);
    }
}

