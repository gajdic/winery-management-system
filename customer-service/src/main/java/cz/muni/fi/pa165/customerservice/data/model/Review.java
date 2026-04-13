package cz.muni.fi.pa165.customerservice.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private UUID wineId;
    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private int rating;
    private String comment;
}
