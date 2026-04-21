package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Request payload used to create a harvest record")
public class HarvestRecordCreateDto {

    @Schema(description = "Identifier of the referenced grape type", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Grape type cannot be null")
    private UUID grapeTypeId;

    @Schema(description = "Identifier of the referenced vineyard", example = "660e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "Vineyard cannot be null")
    private UUID vineyardId;

    @Schema(description = "Date when the harvest took place", example = "2025-09-20")
    @NotNull(message = "Harvest date cannot be null")
    @PastOrPresent
    private LocalDate harvestDate;

    @Schema(description = "Harvested quantity in kilograms", example = "1250.5")
    @Positive(message = "Quantity must be greater than 0")
    private double quantity;

    @Schema(description = "Quality rating of the harvest on a 1 to 10 scale", example = "8")
    @Min(value = 1, message = "Quality must be at least 1")
    @Max(value = 10, message = "Quality must be at most 10")
    private int quality;

    public HarvestRecordCreateDto(UUID grapeTypeId, UUID vineyardId, LocalDate harvestDate, double quantity, int quality) {
        this.grapeTypeId = grapeTypeId;
        this.vineyardId = vineyardId;
        this.harvestDate = harvestDate;
        this.quantity = quantity;
        this.quality = quality;
    }
}
