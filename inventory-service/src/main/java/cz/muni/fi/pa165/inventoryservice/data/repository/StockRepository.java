package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    Optional<Stock> findByWineId(UUID wineId);
}
