package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
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
    void createRecipe_returnsCreated() {
        WineRecipeCreateDto request = new WineRecipeCreateDto();
        WineRecipeDto responseDto = new WineRecipeDto();

        Mockito.when(wineRecipeFacade.createRecipe(request))
            .thenReturn(responseDto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.createRecipe(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void getRecipes_returnsOk() {
        List<WineRecipeDto> list = List.of(
            new WineRecipeDto(),
            new WineRecipeDto()
        );

        Mockito.when(wineRecipeFacade.findAllRecipes())
            .thenReturn(list);

        ResponseEntity<List<WineRecipeDto>> response =
            wineRecipeController.getRecipes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    void getRecipe_returnsOk() {
        UUID id = UUID.randomUUID();
        WineRecipeDto dto = new WineRecipeDto();

        Mockito.when(wineRecipeFacade.findRecipe(id))
            .thenReturn(dto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.getRecipe(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void updateRecipe_returnsOk() {
        UUID id = UUID.randomUUID();
        WineRecipeUpdateDto request = new WineRecipeUpdateDto();
        WineRecipeDto responseDto = new WineRecipeDto();

        Mockito.when(wineRecipeFacade.updateRecipe(id, request))
            .thenReturn(responseDto);

        ResponseEntity<WineRecipeDto> response =
            wineRecipeController.updateRecipe(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void deleteRecipe_returnsNoContent() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response =
            wineRecipeController.deleteRecipe(id);

        Mockito.verify(wineRecipeFacade).deleteRecipe(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
