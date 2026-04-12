package cz.muni.fi.pa165.productionservice.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class HarvestLot {
    private UUID grapeTypeId;
    private UUID vineyardId;
    private LocalDate harvestDate;
    private double availableKilograms;
    private int qualityScore;
}
