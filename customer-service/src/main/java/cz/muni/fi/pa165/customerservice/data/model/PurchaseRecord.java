package cz.muni.fi.pa165.customerservice.data.model;

import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(exclude = {"customer"})
@ToString
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private UUID wineId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double pricePerBottle;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;


    public PurchaseRecord(Customer customer, UUID wineId, int quantity, double pricePerBottle, LocalDateTime purchaseDate) {
        this.customer = customer;
        this.wineId = wineId;
        this.quantity = quantity;
        this.pricePerBottle = pricePerBottle;
        this.purchaseDate = purchaseDate;
    }
}
