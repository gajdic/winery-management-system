package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Body for creating a new customer")
public class CustomerCreateDto {

    @NotBlank
    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;

    @Email
    @NotBlank
    @Schema(description = "Email address of the customer", example = "johndoe@gmail.com")
    private String email;

    @Valid
    @NotNull
    @Schema(description = "Address of the customer")
    private AddressDto address;

    public CustomerCreateDto(String name, String email, AddressDto address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
