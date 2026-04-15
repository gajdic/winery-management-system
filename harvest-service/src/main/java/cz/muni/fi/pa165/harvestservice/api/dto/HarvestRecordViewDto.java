package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Detailed view of a harvest record with nested vineyard and grape type references")
public class HarvestRecordViewDto {

    @Schema(description = "Unique identifier of the harvest record", example = "770e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Basic grape type information associated with the harvest record")
    private GrapeTypeBasicDto grapeType;

    @Schema(description = "Basic vineyard information associated with the harvest record")
    private VineyardBasicDto vineyard;

    @Schema(description = "Date when the harvest took place", example = "2025-09-20")
    private LocalDate harvestDate;

    @Schema(description = "Harvested quantity in kilograms", example = "1250.5")
    private double quantity;

    @Schema(description = "Quality rating of the harvest on a 1 to 10 scale", example = "8")
    private int quality;

    public HarvestRecordViewDto( UUID id, GrapeTypeBasicDto grapeType,VineyardBasicDto vineyard, LocalDate harvestDate, double quantity, int quality) {
        this.id = id;
        this.grapeType = grapeType;
        this.vineyard = vineyard;
        this.harvestDate = harvestDate;
        this.quantity = quantity;
        this.quality = quality;
    }
}
