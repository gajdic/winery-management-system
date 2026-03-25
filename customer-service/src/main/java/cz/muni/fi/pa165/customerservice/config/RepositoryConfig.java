package cz.muni.fi.pa165.customerservice.config;

import cz.muni.fi.pa165.customerservice.data.repository.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }

    public ReviewRepository reviewRepository() {
        return new InMemoryReviewRepository();
    }

    public PurchaseRecordRepository purchaseRecordRepository() {
        return new InMemoryPurchaseRecordRepository();
    }
}
