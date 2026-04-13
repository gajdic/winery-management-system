package cz.muni.fi.pa165.customerservice.data.model.customer;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Table
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {

    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String postCode;
    @Column(nullable = false)
    private String country;

    public Address(String street, String city, String postCode, String country) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }
}
