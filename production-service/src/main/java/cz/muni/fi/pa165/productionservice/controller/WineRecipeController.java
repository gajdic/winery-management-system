package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeUpdateDto;
import cz.muni.fi.pa165.productionservice.config.SecurityConfig;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/production/recipes")
@Tag(name = "Wine Recipe", description = "Endpoints for managing wine recipes")
public class WineRecipeController {
    private final WineRecipeFacade wineRecipeFacade;

    @Autowired
    public WineRecipeController(WineRecipeFacade wineRecipeFacade) {
        this.wineRecipeFacade = wineRecipeFacade;
    }

    @PostMapping
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SecurityScopes.WRITE_SCOPE)
    @Operation(
        summary = "Create a wine recipe",
        description = "Creates a recipe containing wine metadata, required grape composition, and optional additional ingredients."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Wine recipe created",
            content = @Content(schema = @Schema(implementation = WineRecipeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<WineRecipeDto> createRecipe(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Wine recipe data to create.",
            required = true,
            content = @Content(schema = @Schema(implementation = WineRecipeCreateDto.class))
        )
        @RequestBody WineRecipeCreateDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wineRecipeFacade.createRecipe(dto));
    }

    @GetMapping
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SecurityScopes.READ_SCOPE)
    @Operation(
        summary = "Get all wine recipes",
        description = "Returns all wine recipes known to the production service."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Wine recipes returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WineRecipeDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<WineRecipeDto>> getRecipes() {
        return ResponseEntity.ok(wineRecipeFacade.findAllRecipes());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SecurityScopes.READ_SCOPE)
    @Operation(
        summary = "Get a wine recipe by id",
        description = "Returns a single wine recipe identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Wine recipe returned",
            content = @Content(schema = @Schema(implementation = WineRecipeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Wine recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<WineRecipeDto> getRecipe(
        @Parameter(description = "Wine recipe identifier.", example = "33333333-3333-3333-3333-333333333333")
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(wineRecipeFacade.findRecipe(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SecurityScopes.WRITE_SCOPE)
    @Operation(
        summary = "Update a wine recipe by id",
        description = "Replaces the recipe fields, grape composition, and additional ingredients for an existing wine recipe."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Wine recipe updated",
            content = @Content(schema = @Schema(implementation = WineRecipeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format or request body",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Wine recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<WineRecipeDto> updateRecipe(
        @Parameter(description = "Wine recipe identifier.", example = "33333333-3333-3333-3333-333333333333")
        @PathVariable UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated wine recipe data.",
            required = true,
            content = @Content(schema = @Schema(implementation = WineRecipeUpdateDto.class))
        )
        @Valid @RequestBody WineRecipeUpdateDto dto
    ) {
        return ResponseEntity.ok(wineRecipeFacade.updateRecipe(id, dto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME, scopes = SecurityScopes.WRITE_SCOPE)
    @Operation(
        summary = "Delete a wine recipe by id",
        description = "Deletes an existing wine recipe identified by UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Wine recipe deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Wine recipe not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteRecipe(
        @Parameter(description = "Wine recipe identifier.", example = "33333333-3333-3333-3333-333333333333")
        @PathVariable UUID id
    ) {
        wineRecipeFacade.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
