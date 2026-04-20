package cz.muni.fi.pa165.customerservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Information about an existing purchase record")
public class PurchaseRecordDto {

    @NotNull
    @Schema(description = "Unique identifier of the purchase record", example = "e58ed763-928c-4155-bee9-fdbaaadc15f3")
    private UUID id;

    @NotNull
    @Schema(description = "Id of the customer") //TODO
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

    @DateTimeFormat
    @PastOrPresent
    @Schema(description = "Purchase date of the order", example = "2025-03-15T14:30:00")
    private LocalDateTime purchaseDate;
}
