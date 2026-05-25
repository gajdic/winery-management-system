package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Body for creating a new purchase record")
public class PurchaseRecordCreateDto {

    @NotNull
    @Schema(description = "Id of the customer who purchased the wine", example = "44444444-4444-4444-4444-444444444444")
    private UUID customerId;

    @NotNull
    @Schema(description = "Id of the wine the purchase record is about", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID wineId;

    @Min(1)
    @Schema(description = "The amount of bottles bought", example = "7")
    private int quantity;

    @Min(0)
    @Schema(description = "Price per one bottle bought", example = "8.9")
    private double pricePerBottle;
}
