package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.facade.GrapeTypeFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/grape-types")
@Tag(name = "Grape Types", description = "Endpoints for managing grape varieties")
public class GrapeTypeRestController {

    private final GrapeTypeFacade grapeTypeFacade;

    @Autowired
    public GrapeTypeRestController(GrapeTypeFacade grapeTypeFacade) {
        this.grapeTypeFacade = grapeTypeFacade;
    }

    @PostMapping
    @Operation(summary = "Create a grape type", description = "Creates a new grape variety")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Grape type created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid grape type payload")
    })
    public ResponseEntity<GrapeTypeViewDto> create(@RequestBody @Valid GrapeTypeCreateDto dto) {
        return ResponseEntity.status(201).body(grapeTypeFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a grape type by id", description = "Returns one grape type detail by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type found"),
        @ApiResponse(responseCode = "404", description = "Grape type not found")
    })
    public ResponseEntity<GrapeTypeViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(grapeTypeFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    @Operation(summary = "Get a grape type by name", description = "Finds one grape type by its business name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type found"),
        @ApiResponse(responseCode = "404", description = "Grape type not found")
    })
    public ResponseEntity<GrapeTypeViewDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(grapeTypeFacade.findByName(name));
    }

    @GetMapping
    @Operation(summary = "List all grape types", description = "Returns all grape varieties currently registered in the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape types returned successfully")
    })
    public ResponseEntity<List<GrapeTypeViewDto>> findAll() {
        return ResponseEntity.ok(grapeTypeFacade.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a grape type", description = "Updates an existing grape variety by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid grape type payload"),
        @ApiResponse(responseCode = "404", description = "Grape type not found")
    })
    public ResponseEntity<GrapeTypeViewDto> update(@PathVariable UUID id, @RequestBody @Valid GrapeTypeUpdateDto dto) {
        return ResponseEntity.ok(grapeTypeFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grape type", description = "Removes a grape variety from the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Grape type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Grape type not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        grapeTypeFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
