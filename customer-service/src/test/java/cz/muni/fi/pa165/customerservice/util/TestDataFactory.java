package cz.muni.fi.pa165.customerservice.util;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TestDataFactory {

    public static final UUID WINE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private TestDataFactory() {}

    public static Customer customerEntity() {
        Customer c = new Customer();
        c.setName("John Doe");
        c.setEmail("john" + UUID.randomUUID() + "@example.com");
        c.setAddress(addressEntity());
        return c;
    }

    public static Address addressEntity() {
        return new Address(
            "Main Street 1",
            "Brno",
            "60200",
            "Czech Republic"
        );
    }

    public static Review reviewEntity() {
        Review r = new Review();
        r.setCustomer(customerEntity());
        r.setWineId(WINE_ID);
        r.setRating(5);
        r.setComment("Great wine!");
        return r;
    }

    public static PurchaseRecord purchaseRecordEntity() {
        PurchaseRecord p = new PurchaseRecord();
        p.setCustomer(customerEntity());
        p.setWineId(WINE_ID);
        p.setQuantity(5);
        p.setPricePerBottle(10.0);
        p.setPurchaseDate(LocalDateTime.now());
        return p;
    }
}
