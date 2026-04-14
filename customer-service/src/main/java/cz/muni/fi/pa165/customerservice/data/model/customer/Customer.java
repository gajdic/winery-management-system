package cz.muni.fi.pa165.customerservice.data.model.customer;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

import java.util.UUID;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Embedded
    private Address address;

    public Customer(String name, String email, Address address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
