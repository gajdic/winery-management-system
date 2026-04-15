package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.facade.VineyardFacade;
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
@RequestMapping(path = "/vineyards")
@Tag(name = "Vineyards", description = "Endpoints for managing vineyards")
public class VineyardRestController {

    private final VineyardFacade vineyardFacade;

    @Autowired
    public VineyardRestController(VineyardFacade vineyardFacade) {
        this.vineyardFacade = vineyardFacade;
    }

    @PostMapping
    @Operation(summary = "Create a vineyard", description = "Creates a vineyard that can later be referenced by harvest records")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vineyard created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid vineyard payload")
    })
    public ResponseEntity<VineyardViewDto> create(@RequestBody @Valid VineyardCreateDto dto) {
        return ResponseEntity.status(201).body(vineyardFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a vineyard by id", description = "Returns one vineyard detail by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard found"),
        @ApiResponse(responseCode = "404", description = "Vineyard not found")
    })
    public ResponseEntity<VineyardViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(vineyardFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    @Operation(summary = "Get a vineyard by name", description = "Finds one vineyard by its business name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard found"),
        @ApiResponse(responseCode = "404", description = "Vineyard not found")
    })
    public ResponseEntity<VineyardViewDto> findByName(@PathVariable String name) {
        return  ResponseEntity.ok(vineyardFacade.findByName(name));
    }

    @GetMapping
    @Operation(summary = "List all vineyards", description = "Returns all vineyards currently registered in the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyards returned successfully")
    })
    public ResponseEntity<List<VineyardViewDto>> findAll() {
        return ResponseEntity.ok(vineyardFacade.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vineyard", description = "Updates an existing vineyard by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid vineyard payload"),
        @ApiResponse(responseCode = "404", description = "Vineyard not found")
    })
    public ResponseEntity<VineyardViewDto> update(@PathVariable UUID id, @RequestBody @Valid VineyardUpdateDto dto) {
        return ResponseEntity.ok(vineyardFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vineyard", description = "Removes a vineyard from the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vineyard deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Vineyard not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vineyardFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
