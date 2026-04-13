package cz.muni.fi.pa165.productionservice.data.model;

import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "production_batches")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "id")
public class ProductionBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID recipeId;
    private double plannedLiters;
    private int plannedBottleCount;
    private int actualBottleCount;
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private LocalDateTime createdAt;
}
