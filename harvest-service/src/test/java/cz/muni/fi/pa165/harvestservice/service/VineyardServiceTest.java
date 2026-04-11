package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
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
class VineyardServiceTest {

    @Mock
    private VineyardRepository vineyardRepository;

    @InjectMocks
    private VineyardService vineyardService;

    @Test
    void create_validVineyard_returnsSavedVineyard() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        Mockito.when(vineyardRepository.save(vineyard)).thenReturn(vineyard);

        // Act
        Vineyard result = vineyardService.create(vineyard);

        // Assert
        assertThat(result).isEqualTo(vineyard);
        Mockito.verify(vineyardRepository).save(vineyard);
    }

    @Test
    void create_vineyardIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> vineyardService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vineyard cannot be null.");
    }

    @Test
    void create_vineyardIdIsNull_returnsSavedVineyard() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        vineyard.setId(null);
        Mockito.when(vineyardRepository.save(vineyard)).thenReturn(vineyard);

        // Act
        Vineyard result = vineyardService.create(vineyard);

        // Assert
        assertThat(result).isEqualTo(vineyard);
        Mockito.verify(vineyardRepository).save(vineyard);
    }

    @Test
    void create_vineyardNameIsBlank_throwsIllegalArgumentException() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        vineyard.setName("  ");

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.create(vineyard))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vineyard name cannot be null or blank.");
    }

    @Test
    void create_locationIsBlank_throwsIllegalArgumentException() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        vineyard.setLocation("");

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.create(vineyard))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Location cannot be null or blank.");
    }

    @Test
    void create_areaSizeIsZero_throwsIllegalArgumentException() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        vineyard.setAreaSize(0);

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.create(vineyard))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Area size must be greater than 0.");
    }

    @Test
    void findById_vineyardFound_returnsVineyard() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(vineyard));

        // Act
        Vineyard result = vineyardService.findById(TestDataFactory.VINEYARD_ID);

        // Assert
        assertThat(result).isEqualTo(vineyard);
    }

    @Test
    void findById_vineyardNotFound_throwsResourceNotFoundException() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        Mockito.when(vineyardRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.findById(unknownId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vineyard with id: " + unknownId + " was not found.");
    }

    @Test
    void findByName_vineyardFound_returnsVineyard() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        Mockito.when(vineyardRepository.findByName("Moravské vinice"))
                .thenReturn(Optional.of(vineyard));

        // Act
        Vineyard result = vineyardService.findByName("Moravské vinice");

        // Assert
        assertThat(result).isEqualTo(vineyard);
    }

    @Test
    void findByName_vineyardNotFound_throwsResourceNotFoundException() {
        // Arrange
        Mockito.when(vineyardRepository.findByName("Neexistující")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.findByName("Neexistující"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vineyard with name: Neexistující was not found.");
    }

    @Test
    void findAll_repositoryReturnsVineyards_returnsList() {
        // Arrange
        List<Vineyard> vineyards = List.of(TestDataFactory.vineyardEntity());
        Mockito.when(vineyardRepository.findAll()).thenReturn(vineyards);

        // Act
        List<Vineyard> result = vineyardService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(vineyards);
    }

    @Test
    void update_validVineyard_returnsUpdatedVineyard() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        Mockito.when(vineyardRepository.findById(vineyard.getId())).thenReturn(Optional.of(vineyard));
        Mockito.when(vineyardRepository.save(vineyard)).thenReturn(vineyard);

        // Act
        Vineyard result = vineyardService.update(vineyard);

        // Assert
        assertThat(result).isEqualTo(vineyard);
        Mockito.verify(vineyardRepository).findById(vineyard.getId());
        Mockito.verify(vineyardRepository).save(vineyard);
    }

    @Test
    void update_vineyardIsNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> vineyardService.update(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vineyard cannot be null.");
    }

    @Test
    void update_vineyardNotFound_throwsResourceNotFoundException() {
        // Arrange
        Vineyard vineyard = TestDataFactory.vineyardEntity();
        Mockito.when(vineyardRepository.findById(vineyard.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> vineyardService.update(vineyard))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vineyard with id " + vineyard.getId() + " was not found.");
        Mockito.verify(vineyardRepository).findById(vineyard.getId());
        Mockito.verify(vineyardRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteById_existingVineyard_callsRepository() {
        Mockito.when(vineyardRepository.findById(TestDataFactory.VINEYARD_ID))
                .thenReturn(Optional.of(TestDataFactory.vineyardEntity()));

        // Act
        vineyardService.deleteById(TestDataFactory.VINEYARD_ID);

        // Assert
        Mockito.verify(vineyardRepository).deleteById(TestDataFactory.VINEYARD_ID);
    }
}
