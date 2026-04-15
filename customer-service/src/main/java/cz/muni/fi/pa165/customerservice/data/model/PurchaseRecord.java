package cz.muni.fi.pa165.customerservice.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private UUID wineId;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double pricePerBottle;
    @Column(nullable = false)
    private LocalDateTime purchaseDate;


    public PurchaseRecord(UUID customerId, UUID wineId, int quantity, double pricePerBottle, LocalDateTime purchaseDate) {
        this.customerId = customerId;
        this.wineId = wineId;
        this.quantity = quantity;
        this.pricePerBottle = pricePerBottle;
        this.purchaseDate = purchaseDate;
    }
}
