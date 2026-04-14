package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.commonservice.api.harvest.VineyardCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.mappers.VineyardMapper;
import cz.muni.fi.pa165.harvestservice.service.VineyardService;
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
class VineyardFacadeTest {

    @Mock
    private VineyardService vineyardService;

    @Mock
    private VineyardMapper vineyardMapper;

    @InjectMocks
    private VineyardFacade vineyardFacade;

    @Test
    void create_validDto_mapsAndDelegatesToService() {
        // Arrange
        VineyardCreateDto createDto = TestDataFactory.vineyardCreateDto();
        Vineyard entity = TestDataFactory.vineyardEntity();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();

        Mockito.when(vineyardMapper.mapToEntity(createDto)).thenReturn(entity);
        Mockito.when(vineyardService.create(entity)).thenReturn(entity);
        Mockito.when(vineyardMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        VineyardViewDto result = vineyardFacade.create(createDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(vineyardMapper).mapToEntity(createDto);
        Mockito.verify(vineyardService).create(entity);
        Mockito.verify(vineyardMapper).mapToDto(entity);
    }

    @Test
    void findById_existingId_returnsViewDto() {
        // Arrange
        Vineyard entity = TestDataFactory.vineyardEntity();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();

        Mockito.when(vineyardService.findById(TestDataFactory.VINEYARD_ID)).thenReturn(entity);
        Mockito.when(vineyardMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        VineyardViewDto result = vineyardFacade.findById(TestDataFactory.VINEYARD_ID);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(vineyardService).findById(TestDataFactory.VINEYARD_ID);
    }

    @Test
    void findByName_existingName_returnsViewDto() {
        // Arrange
        Vineyard entity = TestDataFactory.vineyardEntity();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();

        Mockito.when(vineyardService.findByName("Moravské vinice")).thenReturn(entity);
        Mockito.when(vineyardMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        VineyardViewDto result = vineyardFacade.findByName("Moravské vinice");

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(vineyardService).findByName("Moravské vinice");
    }

    @Test
    void findAll_vineyardsExist_returnsMappedList() {
        // Arrange
        List<Vineyard> entities = List.of(TestDataFactory.vineyardEntity());
        List<VineyardViewDto> viewDtos = List.of(TestDataFactory.vineyardViewDto());

        Mockito.when(vineyardService.findAll()).thenReturn(entities);
        Mockito.when(vineyardMapper.mapToList(entities)).thenReturn(viewDtos);

        // Act
        List<VineyardViewDto> result = vineyardFacade.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(viewDtos);
        Mockito.verify(vineyardService).findAll();
    }

    @Test
    void update_validIdAndDto_mapsAndDelegatesToService() {
        // Arrange
        VineyardUpdateDto updateDto = new VineyardUpdateDto(
                "Moravské vinice", "Brno, Morava", 5.5, "Nejstarší vinice na Moravě");
        Vineyard entity = TestDataFactory.vineyardEntity();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();

        Mockito.when(vineyardMapper.mapToEntity(TestDataFactory.VINEYARD_ID, updateDto)).thenReturn(entity);
        Mockito.when(vineyardService.update(entity)).thenReturn(entity);
        Mockito.when(vineyardMapper.mapToDto(entity)).thenReturn(viewDto);

        // Act
        VineyardViewDto result = vineyardFacade.update(TestDataFactory.VINEYARD_ID, updateDto);

        // Assert
        assertThat(result).isEqualTo(viewDto);
        Mockito.verify(vineyardMapper).mapToEntity(TestDataFactory.VINEYARD_ID, updateDto);
        Mockito.verify(vineyardService).update(entity);
    }

    @Test
    void deleteById_existingId_delegatesToService() {
        // Act
        vineyardFacade.deleteById(TestDataFactory.VINEYARD_ID);

        // Assert
        Mockito.verify(vineyardService).deleteById(TestDataFactory.VINEYARD_ID);
    }
}

