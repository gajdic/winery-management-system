package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.config.SecurityConfig;
import cz.muni.fi.pa165.harvestservice.facade.VineyardFacade;
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
@RequestMapping(path = "/vineyards")
@Tag(name = "Vineyards", description = "Endpoints for managing vineyards")
public class VineyardRestController {

    private final VineyardFacade vineyardFacade;

    @Autowired
    public VineyardRestController(VineyardFacade vineyardFacade) {
        this.vineyardFacade = vineyardFacade;
    }

    @PostMapping
    @Operation(summary = "Create a vineyard", description = "Creates a vineyard that can later be referenced by harvest records",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"}))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vineyard created successfully",
            content = @Content(schema = @Schema(implementation = VineyardViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vineyard payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VineyardViewDto> create(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Vineyard data to create.",
            required = true,
            content = @Content(schema = @Schema(implementation = VineyardCreateDto.class))
        )
        @RequestBody VineyardCreateDto dto
    ) {
        return ResponseEntity.status(201).body(vineyardFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a vineyard by id", description = "Returns one vineyard detail by its unique identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard found",
            content = @Content(schema = @Schema(implementation = VineyardViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Vineyard not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VineyardViewDto> findById(
        @Parameter(description = "Vineyard identifier.", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(vineyardFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    @Operation(summary = "Get a vineyard by name", description = "Finds one vineyard by its business name",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard found",
            content = @Content(schema = @Schema(implementation = VineyardViewDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Vineyard not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VineyardViewDto> findByName(
        @Parameter(description = "Business name of the vineyard.", example = "Sunrise Hill")
        @PathVariable String name
    ) {
        return  ResponseEntity.ok(vineyardFacade.findByName(name));
    }

    @GetMapping
    @Operation(summary = "List all vineyards", description = "Returns all vineyards currently registered in the harvest service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_read"}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyards returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = VineyardViewDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_read",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<VineyardViewDto>> findAll() {
        return ResponseEntity.ok(vineyardFacade.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vineyard", description = "Updates an existing vineyard by its identifier",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vineyard updated successfully",
            content = @Content(schema = @Schema(implementation = VineyardViewDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format or vineyard payload",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Vineyard not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VineyardViewDto> update(
        @Parameter(description = "Vineyard identifier.", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated vineyard data.",
            required = true,
            content = @Content(schema = @Schema(implementation = VineyardUpdateDto.class))
        )
        @RequestBody @Valid VineyardUpdateDto dto
    ) {
        return ResponseEntity.ok(vineyardFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vineyard", description = "Removes a vineyard from the harvest service",
        security = @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = {"test_write"}))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vineyard deleted successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_write",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Vineyard not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "Vineyard identifier.", example = "22222222-2222-2222-2222-222222222222")
        @PathVariable UUID id
    ) {
        vineyardFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
