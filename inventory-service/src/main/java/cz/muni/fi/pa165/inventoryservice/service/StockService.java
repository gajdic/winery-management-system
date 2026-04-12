package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
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
        if (stock.getId() != null)
            stock.setId(null);
        return stockRepository.save(stock);
    }

    public Stock findById(UUID id) {
        return stockRepository.findById(id).orElse(null);
    }

    public Stock findByWine(UUID wineId) {
        return stockRepository.findByWineId(wineId).orElse(null);
    }

    public Stock addToStock(UUID id, int quantityToAdd) {
        validatePositiveQuantity(quantityToAdd);

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + id));

        stock.setQuantity(stock.getQuantity() + quantityToAdd);
        return stockRepository.save(stock);
    }

    public Stock removeFromStock(UUID id, int quantityToRemove) {
        validatePositiveQuantity(quantityToRemove);

        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + id));

        int available = stock.getQuantity() - stock.getReservedQuantity();
        if (quantityToRemove > available) {
            throw new IllegalArgumentException("Cannot remove more bottles than available stock");
        }

        stock.setQuantity(stock.getQuantity() - quantityToRemove);
        return stockRepository.save(stock);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock update(Stock stock) {
        return stockRepository.save(stock);
    }

    public boolean delete(UUID id) {
        if (!stockRepository.existsById(id)) {
            return false;
        }
        stockRepository.deleteById(id);
        return true;
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
