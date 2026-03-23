package cz.muni.fi.pa165.inventoryservice.config;

import cz.muni.fi.pa165.inventoryservice.data.repository.ReservationRepository;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public StockRepository stockRepository() {
        return new StockRepository();
    }

    @Bean
    public ReservationRepository reservationRepository() {
        return new ReservationRepository();
    }
}

