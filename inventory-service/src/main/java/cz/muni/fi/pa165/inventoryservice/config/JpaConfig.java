package cz.muni.fi.pa165.inventoryservice.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "cz.muni.fi.pa165")
@EnableJpaRepositories(basePackages = "cz.muni.fi.pa165")
public class JpaConfig {
}
