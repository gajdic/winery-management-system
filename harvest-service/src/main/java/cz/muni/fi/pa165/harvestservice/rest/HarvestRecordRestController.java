package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.facade.HarvestRecordFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @ApiResponse(responseCode = "201", description = "Harvest record created successfully",
            content = @Content(schema = @Schema(implementation = HarvestRecordViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid harvest record payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<HarvestRecordViewDto> create(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Harvest record data to create.",
            required = true,
            content = @Content(schema = @Schema(implementation = HarvestRecordCreateDto.class))
        )
        @RequestBody HarvestRecordCreateDto dto
    ) {
        return ResponseEntity.status(201).body(harvestRecordFacade.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a harvest record by id", description = "Returns one harvest record with nested vineyard and grape type references")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest record found",
            content = @Content(schema = @Schema(implementation = HarvestRecordViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Harvest record not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<HarvestRecordViewDto> findById(
        @Parameter(description = "Harvest record identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(harvestRecordFacade.findById(id));
    }

    @GetMapping("/by-year/{year}")
    @Operation(summary = "List harvest records by year", description = "Returns all harvest records whose harvest date falls into the given year")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestRecordViewDto.class)))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByYear(
        @Parameter(description = "Harvest year to filter by.", example = "2025")
        @PathVariable int year
    ) {
        return ResponseEntity.ok(harvestRecordFacade.findByYear(year));
    }

    @GetMapping("/by-vineyard/{id}")
    @Operation(summary = "List harvest records by vineyard", description = "Returns all harvest records associated with the selected vineyard")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestRecordViewDto.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Vineyard not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByVineyardId(
        @Parameter(description = "Vineyard identifier.", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(harvestRecordFacade.findByVineyardId(id));
    }

    @GetMapping("/by-grape-type/{id}")
    @Operation(summary = "List harvest records by grape type", description = "Returns all harvest records associated with the selected grape variety")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestRecordViewDto.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Grape type not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findByGrapeTypeId(
        @Parameter(description = "Grape type identifier.", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(harvestRecordFacade.findByGrapeTypeId(id));
    }

    @GetMapping
    @Operation(summary = "List all harvest records", description = "Returns the complete harvest history stored in the harvest service")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest records returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestRecordViewDto.class)))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestRecordViewDto>> findAll() {
        return ResponseEntity.ok(harvestRecordFacade.findAll());
    }

    @GetMapping("/summary/by-year")
    @Operation(summary = "Summarize harvest by year", description = "Aggregates harvested quantity by year across all recorded harvests")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest summary by year returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestSummaryDto.class)))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestSummaryDto>> summaryByYear() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByYear());
    }

    @GetMapping("/summary/by-vineyard")
    @Operation(summary = "Summarize harvest by vineyard", description = "Aggregates harvested quantity by vineyard across all recorded harvests")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest summary by vineyard returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HarvestSummaryDto.class)))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<HarvestSummaryDto>> summaryByVineyard() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByVineyard());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a harvest record", description = "Updates an existing harvest record by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Harvest record updated successfully",
            content = @Content(schema = @Schema(implementation = HarvestRecordViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format or harvest record payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Harvest record not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<HarvestRecordViewDto> update(
        @Parameter(description = "Harvest record identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated harvest record data.",
            required = true,
            content = @Content(schema = @Schema(implementation = HarvestRecordUpdateDto.class))
        )
        @RequestBody @Valid HarvestRecordUpdateDto dto
    ) {
        return ResponseEntity.ok(harvestRecordFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a harvest record", description = "Removes a harvest record from the harvest history")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Harvest record deleted successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Harvest record not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "Harvest record identifier.", example = "44444444-4444-4444-4444-444444444444")
        @PathVariable UUID id
    ) {
        harvestRecordFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
