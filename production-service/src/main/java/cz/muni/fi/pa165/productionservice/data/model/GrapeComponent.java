package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GrapeComponent {
    private String grapeType;
    private double kilogramsPerLiter;
    private int preferredQuality;
}
