package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.config.SecurityConfig;
import cz.muni.fi.pa165.harvestservice.facade.GrapeTypeFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Create a grape type", description = "Creates a new grape variety",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Grape type created successfully",
            content = @Content(schema = @Schema(implementation = GrapeTypeViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid grape type payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<GrapeTypeViewDto> create(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Grape type data to create.",
            required = true,
            content = @Content(schema = @Schema(implementation = GrapeTypeCreateDto.class))
        )
        @RequestBody GrapeTypeCreateDto dto
    ) {
        return ResponseEntity.status(201).body(grapeTypeFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a grape type by id", description = "Returns one grape type detail by its unique identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type found",
            content = @Content(schema = @Schema(implementation = GrapeTypeViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Grape type not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<GrapeTypeViewDto> findById(
        @Parameter(description = "Grape type identifier.", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(grapeTypeFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    @Operation(summary = "Get a grape type by name", description = "Finds one grape type by its business name",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type found",
            content = @Content(schema = @Schema(implementation = GrapeTypeViewDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Grape type not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<GrapeTypeViewDto> findByName(
        @Parameter(description = "Business name of the grape type.", example = "Riesling")
        @PathVariable String name
    ) {
        return ResponseEntity.ok(grapeTypeFacade.findByName(name));
    }

    @GetMapping
    @Operation(summary = "List all grape types", description = "Returns all grape varieties currently registered in the harvest service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.READ_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape types returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GrapeTypeViewDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<GrapeTypeViewDto>> findAll() {
        return ResponseEntity.ok(grapeTypeFacade.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a grape type", description = "Updates an existing grape variety by its identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grape type updated successfully",
            content = @Content(schema = @Schema(implementation = GrapeTypeViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format or grape type payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Grape type not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<GrapeTypeViewDto> update(
        @Parameter(description = "Grape type identifier.", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated grape type data.",
            required = true,
            content = @Content(schema = @Schema(implementation = GrapeTypeUpdateDto.class))
        )
        @RequestBody @Valid GrapeTypeUpdateDto dto
    ) {
        return ResponseEntity.ok(grapeTypeFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grape type", description = "Removes a grape variety from the harvest service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {SecurityScopes.WRITE_SCOPE}))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Grape type deleted successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Grape type not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "Grape type identifier.", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        grapeTypeFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
