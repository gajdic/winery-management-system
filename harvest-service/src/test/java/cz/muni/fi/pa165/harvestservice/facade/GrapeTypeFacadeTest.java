package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.mappers.GrapeTypeMapper;
import cz.muni.fi.pa165.harvestservice.service.GrapeTypeService;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GrapeTypeFacadeTest {

    @Mock
    private GrapeTypeService grapeTypeService;

    @Mock
    private GrapeTypeMapper grapeTypeMapper;

    @InjectMocks
    private GrapeTypeFacade grapeTypeFacade;

    @Test
    void create_validDto_mapsAndDelegatesToService() {
        // Arrange
        GrapeTypeCreateDto createDto = TestDataFactory.grapeTypeCreateDto();
        GrapeType entity = TestDataFactory.grapeTypeEntity();
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();

        Mockito.when(grapeTypeMapper.mapToEntity(createDto)).thenReturn(entity);
        Mockito.when(grapeTypeService.create(entity)).thenReturn(entity);
        Mockito.when(grapeTypeMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        GrapeTypeViewDto result = grapeTypeFacade.create(createDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(grapeTypeMapper).mapToEntity(createDto);
        Mockito.verify(grapeTypeService).create(entity);
        Mockito.verify(grapeTypeMapper).mapToDto(entity);
    }

    @Test
    void findById_existingId_returnsViewDto() {
        // Arrange
        GrapeType entity = TestDataFactory.grapeTypeEntity();
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();

        Mockito.when(grapeTypeService.findById(TestDataFactory.GRAPE_TYPE_ID)).thenReturn(entity);
        Mockito.when(grapeTypeMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        GrapeTypeViewDto result = grapeTypeFacade.findById(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(grapeTypeService).findById(TestDataFactory.GRAPE_TYPE_ID);
    }

    @Test
    void findByName_existingName_returnsViewDto() {
        // Arrange
        GrapeType entity = TestDataFactory.grapeTypeEntity();
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();

        Mockito.when(grapeTypeService.findByName("Welschriesling")).thenReturn(entity);
        Mockito.when(grapeTypeMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        GrapeTypeViewDto result = grapeTypeFacade.findByName("Welschriesling");

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(grapeTypeService).findByName("Welschriesling");
    }

    @Test
    void findAll_grapeTypesExist_returnsMappedList() {
        // Arrange
        List<GrapeType> entities = List.of(TestDataFactory.grapeTypeEntity());
        List<GrapeTypeViewDto> viewDtos = List.of(TestDataFactory.grapeTypeViewDto());

        Mockito.when(grapeTypeService.findAll()).thenReturn(entities);
        Mockito.when(grapeTypeMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<GrapeTypeViewDto> result = grapeTypeFacade.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(grapeTypeService).findAll();
    }

    @Test
    void update_validIdAndDto_mapsAndDelegatesToService() {
        // Arrange
        GrapeTypeUpdateDto updateDto = new GrapeTypeUpdateDto("Welschriesling", "Oblíbená odrůda na Moravě");
        GrapeType entity = TestDataFactory.grapeTypeEntity();
        GrapeTypeViewDto viewDto = TestDataFactory.grapeTypeViewDto();

        Mockito.when(grapeTypeMapper.mapToEntity(TestDataFactory.GRAPE_TYPE_ID, updateDto)).thenReturn(entity);
        Mockito.when(grapeTypeService.update(entity)).thenReturn(entity);
        Mockito.when(grapeTypeMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        GrapeTypeViewDto result = grapeTypeFacade.update(TestDataFactory.GRAPE_TYPE_ID, updateDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(grapeTypeMapper).mapToEntity(TestDataFactory.GRAPE_TYPE_ID, updateDto);
        Mockito.verify(grapeTypeService).update(entity);
    }

    @Test
    void deleteById_existingId_delegatesToService() {
        // Act
        grapeTypeFacade.deleteById(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        Mockito.verify(grapeTypeService).deleteById(TestDataFactory.GRAPE_TYPE_ID);
    }
}
