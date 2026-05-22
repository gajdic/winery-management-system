package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.commonservice.rest.exceptionhandling.ApiError;
import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.config.SecurityConfig;
import cz.muni.fi.pa165.customerservice.facade.CustomerFacade;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerFacade customerFacade;

    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @PostMapping
    @Operation(
        summary = "Create a new customer",
        description = "Creates a new customer with provided personal and address data.",
        security = @SecurityRequirement(
            name = SecurityConfig.SECURITY_SCHEME_NAME,
            scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Customer created",
            content = @Content(schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomerDto> createCustomer(
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Customer creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = CustomerCreateDto.class))
        )
        @RequestBody CustomerCreateDto dto
    ) {
        CustomerDto created = customerFacade.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get customer by ID",
        description = "Returns a customer identified by UUID.",
        security = @SecurityRequirement(
            name = SecurityConfig.SECURITY_SCHEME_NAME,
            scopes = {SecurityScopes.READ_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer found",
            content = @Content(schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomerDto> getCustomer(
        @Parameter(description = "Customer ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        CustomerDto customer = customerFacade.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @Operation(
        summary = "Get all customers",
        description = "Returns a list of all customers.",
        security = @SecurityRequirement(
            name = SecurityConfig.SECURITY_SCHEME_NAME,
            scopes = {SecurityScopes.READ_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customers retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerFacade.getAllCustomers());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update customer",
        description = "Updates an existing customer identified by UUID.",
        security = @SecurityRequirement(
            name = SecurityConfig.SECURITY_SCHEME_NAME,
            scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer updated",
            content = @Content(schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomerDto> updateCustomer(
        @Parameter(description = "Customer ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id,
        @Valid
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Customer update request",
            required = true,
            content = @Content(schema = @Schema(implementation = CustomerUpdateDto.class))
        )
        @RequestBody CustomerUpdateDto dto
    ) {
        CustomerDto updated = customerFacade.updateCustomer(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete customer",
        description = "Deletes a customer identified by UUID.",
        security = @SecurityRequirement(
            name = SecurityConfig.SECURITY_SCHEME_NAME,
            scopes = {SecurityScopes.WRITE_SCOPE})
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Customer deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid UUID format",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - missing required scope",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteCustomer(
        @Parameter(description = "Customer ID", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable UUID id
    ) {
        customerFacade.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
