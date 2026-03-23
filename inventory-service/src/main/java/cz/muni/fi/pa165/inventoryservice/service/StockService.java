package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return stockRepository.findById(id);
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
}
