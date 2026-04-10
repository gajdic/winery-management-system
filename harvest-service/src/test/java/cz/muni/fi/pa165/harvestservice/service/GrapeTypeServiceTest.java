package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class GrapeTypeServiceTest {

    @Mock
    private GrapeTypeRepository grapeTypeRepository;

    @InjectMocks
    private GrapeTypeService grapeTypeService;

    @Test
    void create_validGrapeType_returnsSavedGrapeType() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        Mockito.when(grapeTypeRepository.save(grapeType)).thenReturn(grapeType);

        // Act
        GrapeType result = grapeTypeService.create(grapeType);

        // Assert
        assertThat(result).isEqualTo(grapeType);
        Mockito.verify(grapeTypeRepository).save(grapeType);
    }

    @Test
    void create_grapeTypeIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> grapeTypeService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type cannot be null");
    }

    @Test
    void create_nameIsNull_throwsIllegalArgumentException() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        grapeType.setName(null);

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.create(grapeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type name cannot be null or blank");
    }

    @Test
    void create_nameIsBlank_throwsIllegalArgumentException() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        grapeType.setName("   ");

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.create(grapeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type name cannot be null or blank");
    }

    @Test
    void findById_grapeTypeFound_returnsGrapeType() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        Mockito.when(grapeTypeRepository.findById(TestDataFactory.GRAPE_TYPE_ID))
                .thenReturn(Optional.of(grapeType));

        // Act
        GrapeType result = grapeTypeService.findById(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        assertThat(result).isEqualTo(grapeType);
    }

    @Test
    void findById_grapeTypeNotFound_throwsResourceNotFoundException() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        Mockito.when(grapeTypeRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.findById(unknownId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Grape type with id: " + unknownId + " was not found.");
    }

    @Test
    void findByName_grapeTypeFound_returnsGrapeType() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        Mockito.when(grapeTypeRepository.findByName("Welschriesling"))
                .thenReturn(Optional.of(grapeType));

        // Act
        GrapeType result = grapeTypeService.findByName("Welschriesling");

        // Assert
        assertThat(result).isEqualTo(grapeType);
    }

    @Test
    void findByName_grapeTypeNotFound_throwsResourceNotFoundException() {
        // Arrange
        Mockito.when(grapeTypeRepository.findByName("Neexistující")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.findByName("Neexistující"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Grape type with name: Neexistující was not found.");
    }

    @Test
    void findAll_repositoryReturnsGrapeTypes_returnsList() {
        // Arrange
        List<GrapeType> grapeTypes = List.of(TestDataFactory.grapeTypeEntity());
        Mockito.when(grapeTypeRepository.findAll()).thenReturn(grapeTypes);

        // Act
        List<GrapeType> result = grapeTypeService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(grapeTypes);
    }

    @Test
    void update_validGrapeType_returnsUpdatedGrapeType() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        Mockito.when(grapeTypeRepository.findById(grapeType.getId())).thenReturn(Optional.of(grapeType));
        Mockito.when(grapeTypeRepository.save(grapeType)).thenReturn(grapeType);

        // Act
        GrapeType result = grapeTypeService.update(grapeType);

        // Assert
        assertThat(result).isEqualTo(grapeType);
        Mockito.verify(grapeTypeRepository).findById(grapeType.getId());
        Mockito.verify(grapeTypeRepository).save(grapeType);
    }

    @Test
    void update_grapeTypeIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> grapeTypeService.update(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type cannot be null");
    }

    @Test
    void update_idIsNull_throwsIllegalArgumentException() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        grapeType.setId(null);

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.update(grapeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grape type id cannot be null");
    }

    @Test
    void update_grapeTypeNotFound_throwsResourceNotFoundException() {
        // Arrange
        GrapeType grapeType = TestDataFactory.grapeTypeEntity();
        Mockito.when(grapeTypeRepository.findById(grapeType.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> grapeTypeService.update(grapeType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Grape type with id: " + grapeType.getId() + " was not found.");
        Mockito.verify(grapeTypeRepository).findById(grapeType.getId());
        Mockito.verify(grapeTypeRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteById_existingGrapeType_callsRepository() {
        // Act
        grapeTypeService.deleteById(TestDataFactory.GRAPE_TYPE_ID);

        // Assert
        Mockito.verify(grapeTypeRepository).deleteById(TestDataFactory.GRAPE_TYPE_ID);
    }
}
