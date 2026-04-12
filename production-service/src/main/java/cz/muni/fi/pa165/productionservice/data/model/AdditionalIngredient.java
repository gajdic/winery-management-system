package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalIngredient {
    private String name;
    private double quantityPerLiter;
    private String unit;
}
