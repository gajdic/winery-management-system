package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Address of a customer")
public class AddressDto {

    @NotBlank
    @Schema(description = "Street name", example = "Masarova 8")
    private String street;

    @NotBlank
    @Schema(description = "City name", example = "Brno")
    private String city;

    @Pattern(regexp = "\\d{5}", message = "Post code must have 5 digits")
    @Schema(description = "Postal code", example = "60200")
    private String postCode;

    @NotBlank
    @Schema(description = "Country", example = "Czechia")
    private String country;

    public AddressDto(String street, String city, String postCode, String country) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }
}
