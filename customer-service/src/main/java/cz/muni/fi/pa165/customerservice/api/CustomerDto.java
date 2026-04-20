package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Information about an existing customer")
public class CustomerDto {

    @NotNull
    @Schema(description = "Unique identifier of the customer", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID id;

    @NotBlank
    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;

    @Email
    @Schema(description = "Email address of the customer", example = "johndoe@gmail.com")
    private String email;

    @Valid
    @NotNull
    @Schema(description = "Address of the customer")
    private AddressDto address;
}
