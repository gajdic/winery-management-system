package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WineRecipeControllerTest {

    @Mock
    private WineRecipeFacade wineRecipeFacade;

    @InjectMocks
    private WineRecipeController wineRecipeController;

    @Test
    void createRecipe_validRequest_returnsCreated() {
        WineRecipeCreateDto request = TestDataFactory.wineRecipeCreateDto();
        WineRecipeDto responseDto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeFacade.createRecipe(request))
            .thenReturn(responseDto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.createRecipe(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void getRecipes_noInput_returnsOk() {
        List<WineRecipeDto> list = TestDataFactory.wineRecipeDtos();

        Mockito.when(wineRecipeFacade.findAllRecipes())
            .thenReturn(list);

        ResponseEntity<List<WineRecipeDto>> response =
            wineRecipeController.getRecipes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    void getRecipe_existingId_returnsOk() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipeDto dto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeFacade.findRecipe(id))
            .thenReturn(dto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.getRecipe(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void updateRecipe_validRequest_returnsOk() {
        UUID id = TestDataFactory.RECIPE_ID;
        WineRecipeUpdateDto request = TestDataFactory.wineRecipeUpdateDto();
        WineRecipeDto responseDto = TestDataFactory.wineRecipeDto();

        Mockito.when(wineRecipeFacade.updateRecipe(id, request))
            .thenReturn(responseDto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.updateRecipe(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void deleteRecipe_existingId_returnsNoContent() {
        UUID id = TestDataFactory.RECIPE_ID;

        ResponseEntity<Void> response =
            wineRecipeController.deleteRecipe(id);

        Mockito.verify(wineRecipeFacade).deleteRecipe(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
