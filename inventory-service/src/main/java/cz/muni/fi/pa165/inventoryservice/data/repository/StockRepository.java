package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;

import java.util.Optional;
import java.util.UUID;

public class StockRepository extends InMemoryRepository<Stock> {
    public Optional<Stock> findByWine(UUID wineId) {
        return storage.values().stream()
                .filter(stock -> wineId.equals(stock.getWineId()))
                .findFirst();
    }

    public Stock updateQuantity(UUID id, int delta) {
        Stock stock = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + id));

        stock.setQuantity(stock.getQuantity() + delta);
        return update(stock);
    }

    public Stock updateReservedQuantity(UUID id, int delta) {
        Stock stock = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + id));

        stock.setReservedQuantity(stock.getReservedQuantity() + delta);
        return update(stock);
    }
}

