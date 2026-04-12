package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AdditionalIngredient {
    private String name;
    private double quantityPerLiter;
    private String unit;
}
