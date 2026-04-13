package cz.muni.fi.pa165.productionservice.facade;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import cz.muni.fi.pa165.productionservice.mappers.WineRecipeMapper;
import cz.muni.fi.pa165.productionservice.service.WineRecipeService;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WineRecipeFacadeTest {

    @Mock
    private WineRecipeService wineRecipeService;

    @Mock
    private WineRecipeMapper wineRecipeMapper;

    @InjectMocks
    private WineRecipeFacade wineRecipeFacade;

    @Test
    void createRecipe_validRequest_callsServiceAndReturnsDto() {
        WineRecipeCreateDto createDto = TestDataFactory.wineRecipeCreateDto();
        WineRecipe entity = TestDataFactory.wineRecipeEntity();
        WineRecipeDto resultDto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeMapper.mapToEntity(createDto)).thenReturn(entity);
        Mockito.when(wineRecipeService.create(entity)).thenReturn(entity);
        Mockito.when(wineRecipeMapper.mapToDto(entity)).thenReturn(resultDto);

        WineRecipeDto result = wineRecipeFacade.createRecipe(createDto);

        assertThat(result).isEqualTo(resultDto);
    }

    @Test
    void findRecipe_existingId_returnsDto() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipe entity = TestDataFactory.wineRecipeEntity();
        WineRecipeDto dto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeService.findById(id)).thenReturn(entity);
        Mockito.when(wineRecipeMapper.mapToDto(entity)).thenReturn(dto);

        WineRecipeDto result = wineRecipeFacade.findRecipe(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findAllRecipes_noInput_returnsMappedDtos() {
        WineRecipe entity1 = TestDataFactory.wineRecipeEntity("Riesling", 2026);
        WineRecipe entity2 = TestDataFactory.wineRecipeEntity("Pinot Noir", 2025);

        WineRecipeDto dto1 = TestDataFactory.wineRecipeDto();
        WineRecipeDto dto2 = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeService.findAll()).thenReturn(List.of(entity1, entity2));

        Mockito.when(wineRecipeMapper.mapToDto(Mockito.any(WineRecipe.class)))
            .thenReturn(dto1, dto2);

        List<WineRecipeDto> result = wineRecipeFacade.findAllRecipes();

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void updateRecipe_validRequest_callsServiceAndReturnsDto() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipeUpdateDto updateDto = TestDataFactory.wineRecipeUpdateDto();
        WineRecipe updateEntity = TestDataFactory.wineRecipeEntity("Updated Riesling", 2025);
        WineRecipe updatedEntity = TestDataFactory.wineRecipeEntity("Updated Riesling", 2025);
        WineRecipeDto resultDto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeMapper.mapToEntity(updateDto))
            .thenReturn(updateEntity);

        Mockito.when(wineRecipeService.update(id, updateEntity))
            .thenReturn(updatedEntity);

        Mockito.when(wineRecipeMapper.mapToDto(updatedEntity))
            .thenReturn(resultDto);

        WineRecipeDto result = wineRecipeFacade.updateRecipe(id, updateDto);

        assertThat(result).isEqualTo(resultDto);
    }

    @Test
    void deleteRecipe_existingId_callsService() {
        UUID id = TestDataFactory.RECIPE_ID;

        wineRecipeFacade.deleteRecipe(id);

        Mockito.verify(wineRecipeService).deleteById(id);
    }
}


