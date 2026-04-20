package cz.muni.fi.pa165.harvestservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Aggregated harvest summary result")
public class HarvestSummaryDto {

    @Schema(description = "Grouping key used in the summary output", example = "2025")
    private String groupByValue;

    @Schema(description = "Total harvested quantity for the grouping key in kilograms", example = "5420.0")
    private double totalQuantity;

    public HarvestSummaryDto(String groupByValue, double totalQuantity) {
        this.groupByValue = groupByValue;
        this.totalQuantity = totalQuantity;
    }
}
