package cz.muni.fi.pa165.productionservice.rest;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
@Tag(name = "Wine Recipe", description = "Endpoints for managing wine recipes")
public class WineRecipeController {
    private final WineRecipeFacade wineRecipeFacade;

    public WineRecipeController(WineRecipeFacade wineRecipeFacade) {
        this.wineRecipeFacade = wineRecipeFacade;
    }

    @PostMapping("/recipes")
    @Operation(summary = "Create a wine recipe")
    public ResponseEntity<WineRecipeDto> createRecipe(@Valid @RequestBody WineRecipeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wineRecipeFacade.createRecipe(dto));
    }

    @GetMapping("/recipes")
    @Operation(summary = "Get all wine recipes")
    public ResponseEntity<List<WineRecipeDto>> getRecipes() {
        return ResponseEntity.ok(wineRecipeFacade.findAllRecipes());
    }

    @GetMapping("/recipes/{id}")
    @Operation(summary = "Get a wine recipe by id")
    public ResponseEntity<WineRecipeDto> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(wineRecipeFacade.findRecipe(id));
    }
}
