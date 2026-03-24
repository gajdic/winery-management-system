package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/production/recipes")
@Tag(name = "Wine Recipe", description = "Endpoints for managing wine recipes")
public class WineRecipeController {
    private final WineRecipeFacade wineRecipeFacade;

    @Autowired
    public WineRecipeController(WineRecipeFacade wineRecipeFacade) {
        this.wineRecipeFacade = wineRecipeFacade;
    }

    @PostMapping
    @Operation(summary = "Create a wine recipe")
    public ResponseEntity<WineRecipeDto> createRecipe(@Valid @RequestBody WineRecipeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wineRecipeFacade.createRecipe(dto));
    }

    @GetMapping
    @Operation(summary = "Get all wine recipes")
    public ResponseEntity<List<WineRecipeDto>> getRecipes() {
        return ResponseEntity.ok(wineRecipeFacade.findAllRecipes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a wine recipe by id")
    public ResponseEntity<WineRecipeDto> getRecipe(@PathVariable UUID id) {
        return ResponseEntity.ok(wineRecipeFacade.findRecipe(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a wine recipe by id")
    public ResponseEntity<Void> deleteRecipe(@PathVariable UUID id) {
        wineRecipeFacade.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
