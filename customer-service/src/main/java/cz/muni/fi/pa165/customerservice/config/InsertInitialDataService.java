package cz.muni.fi.pa165.customerservice.config;

import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.data.model.Review;
import cz.muni.fi.pa165.customerservice.data.model.customer.Address;
import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import cz.muni.fi.pa165.customerservice.data.repository.CustomerRepository;
import cz.muni.fi.pa165.customerservice.data.repository.PurchaseRecordRepository;
import cz.muni.fi.pa165.customerservice.data.repository.ReviewRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InsertInitialDataService {

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final PurchaseRecordRepository purchaseRecordRepository;

    @Autowired
    public InsertInitialDataService(CustomerRepository customerRepository,
                                    ReviewRepository reviewRepository,
                                    PurchaseRecordRepository purchaseRecordRepository) {
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
        this.purchaseRecordRepository = purchaseRecordRepository;
    }

    @PostConstruct
    public void insertDummyData() {
        // --- Customers ---
        Customer customer1 = new Customer(
            "John Doe",
            "john@example.com",
            new Address("Main St 1", "Brno", "60200", "Czech Republic")
        );
        customerRepository.save(customer1);

        Customer customer2 = new Customer(
            "Jane Smith",
            "jane@example.com",
            new Address("Second St 5", "Prague", "11000", "Czech Republic")
        );
        customerRepository.save(customer2);

        Review review1 = new Review();
        review1.setCustomer(customer1);
        review1.setWineId(UUID.randomUUID());
        review1.setRating(5);
        review1.setComment("Excellent wine!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setCustomer(customer2);
        review2.setWineId(UUID.randomUUID());
        review2.setRating(4);
        review2.setComment("Very good.");
        reviewRepository.save(review2);

        // --- Purchases ---
        PurchaseRecord purchase1 = new PurchaseRecord();
        purchase1.setCustomer(customer1);
        purchase1.setWineId(UUID.randomUUID());
        purchase1.setQuantity(10);
        purchase1.setPricePerBottle(12.5);
        purchase1.setPurchaseDate(LocalDateTime.now());
        purchaseRecordRepository.save(purchase1);

        PurchaseRecord purchase2 = new PurchaseRecord();
        purchase2.setCustomer(customer1);
        purchase2.setWineId(UUID.randomUUID());
        purchase2.setQuantity(3);
        purchase2.setPricePerBottle(20.0);
        purchase2.setPurchaseDate(LocalDateTime.now());
        purchaseRecordRepository.save(purchase2);
    }

    private void insertDummyCustomers() {

    }
}
