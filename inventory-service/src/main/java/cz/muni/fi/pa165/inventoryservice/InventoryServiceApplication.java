package cz.muni.fi.pa165.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "cz.muni.fi.pa165")
@EntityScan(basePackages = "cz.muni.fi.pa165")
@EnableJpaRepositories(basePackages = "cz.muni.fi.pa165")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
