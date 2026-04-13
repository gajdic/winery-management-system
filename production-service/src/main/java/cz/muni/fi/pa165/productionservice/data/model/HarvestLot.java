package cz.muni.fi.pa165.productionservice.data.model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class HarvestLot {
    private UUID grapeTypeId;
    private UUID vineyardId;
    private LocalDate harvestDate;
    private double availableKilograms;
    private int qualityScore;
}
