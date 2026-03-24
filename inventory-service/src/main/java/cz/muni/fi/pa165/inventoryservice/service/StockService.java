package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock create(Stock stock) {
        return stockRepository.create(stock);
    }

    public Stock findById(UUID id) {
        return stockRepository.findById(id).orElse(null);
    }

    public Stock findByWine(UUID wineId) {
        return stockRepository.findByWine(wineId).orElse(null);
    }

    public Stock addToStock(UUID id, int quantityToAdd) {
        validatePositiveQuantity(quantityToAdd);
        return stockRepository.updateQuantity(id, quantityToAdd);
    }

    public Stock removeFromStock(UUID id, int quantityToRemove) {
        validatePositiveQuantity(quantityToRemove);

        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + id));

        int available = stock.getQuantity() - stock.getReservedQuantity();
        if (quantityToRemove > available) {
            throw new IllegalArgumentException("Cannot remove more bottles than available stock");
        }

        return stockRepository.updateQuantity(id, -quantityToRemove);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock update(Stock stock) {
        return stockRepository.update(stock);
    }

    public boolean delete(UUID id) {
        return stockRepository.delete(id);
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
