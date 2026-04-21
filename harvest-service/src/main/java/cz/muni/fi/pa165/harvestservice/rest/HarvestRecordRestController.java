package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.facade.HarvestRecordFacade;
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
@RequestMapping(path = "/harvest-records")
@Tag(name = "Harvest Records", description = "Endpoints for managing harvest records and harvest summaries")
public class HarvestRecordRestController {

    private final HarvestRecordFacade harvestRecordFacade;

    @Autowired
    public HarvestRecordRestController(HarvestRecordFacade harvestRecordFacade) {
        this.harvestRecordFacade = harvestRecordFacade;
    }

    @PostMapping
    @Operation(summary = "Create a harvest record", description = "Creates a harvest record linked to an existing vineyard and grape type")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Harvest record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid harvest record payload")
    })
    public ResponseEntity<HarvestRecordViewDto> create(@RequestBody @Valid HarvestRecordCreateDto dto) {
        return ResponseEntity.status(201).body(harvestRecordFacade.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a harvest record by id", description = "Returns one harvest record with nested vineyard and grape type references")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest record found"),
        @ApiResponse(responseCode = "404", description = "Harvest record not found")
    })
    public ResponseEntity<HarvestRecordViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findById(id));
    }

    @GetMapping("/by-year/{year}")
    @Operation(summary = "List harvest records by year", description = "Returns all harvest records whose harvest date falls into the given year")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully")
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByYear(@PathVariable int year) {
        return ResponseEntity.ok(harvestRecordFacade.findByYear(year));
    }

    @GetMapping("/by-vineyard/{id}")
    @Operation(summary = "List harvest records by vineyard", description = "Returns all harvest records associated with the selected vineyard")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully"),
        @ApiResponse(responseCode = "404", description = "Vineyard not found")
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByVineyardId(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findByVineyardId(id));
    }

    @GetMapping("/by-grape-type/{id}")
    @Operation(summary = "List harvest records by grape type", description = "Returns all harvest records associated with the selected grape variety")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully"),
        @ApiResponse(responseCode = "404", description = "Grape type not found")
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByGrapeTypeId(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findByGrapeTypeId(id));
    }

    @GetMapping
    @Operation(summary = "List all harvest records", description = "Returns the complete harvest history stored in the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully")
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findAll() {
        return ResponseEntity.ok(harvestRecordFacade.findAll());
    }

    @GetMapping("/summary/by-year")
    @Operation(summary = "Summarize harvest by year", description = "Aggregates harvested quantity by year across all recorded harvests")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest summary by year returned successfully")
    })
    public ResponseEntity<List<HarvestSummaryDto>> summaryByYear() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByYear());
    }

    @GetMapping("/summary/by-vineyard")
    @Operation(summary = "Summarize harvest by vineyard", description = "Aggregates harvested quantity by vineyard across all recorded harvests")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest summary by vineyard returned successfully")
    })
    public ResponseEntity<List<HarvestSummaryDto>> summaryByVineyard() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByVineyard());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a harvest record", description = "Updates an existing harvest record by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest record updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid harvest record payload"),
        @ApiResponse(responseCode = "404", description = "Harvest record not found")
    })
    public ResponseEntity<HarvestRecordViewDto> update(@PathVariable UUID id, @RequestBody @Valid HarvestRecordUpdateDto dto) {
        return ResponseEntity.ok(harvestRecordFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a harvest record", description = "Removes a harvest record from the harvest history")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Harvest record deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Harvest record not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        harvestRecordFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
