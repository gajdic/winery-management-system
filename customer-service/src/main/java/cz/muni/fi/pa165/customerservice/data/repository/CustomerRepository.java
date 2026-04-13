package cz.muni.fi.pa165.customerservice.data.repository;

import cz.muni.fi.pa165.customerservice.data.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
