package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrapeComponent {
    private String grapeType;
    private double kilogramsPerLiter;
    private int preferredQuality;
}
