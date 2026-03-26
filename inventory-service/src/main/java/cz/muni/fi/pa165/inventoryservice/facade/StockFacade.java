package cz.muni.fi.pa165.inventoryservice.facade;

import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.mappers.StockMapper;
import cz.muni.fi.pa165.inventoryservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class StockFacade {
    private final StockService stockService;
    private final StockMapper stockMapper;

    @Autowired
    public StockFacade(StockService stockService, StockMapper stockMapper) {
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    public StockDetailedViewDto create(StockDetailedViewDto stock) {
        return stockMapper.mapToDetailViewDto(stockService.create(stockMapper.mapToEntity(stock)));
    }

    public StockDetailedViewDto findById(UUID id) {
        Stock stock = stockService.findById(id);
        return stock == null ? null : stockMapper.mapToDetailViewDto(stock);
    }

    public StockDetailedViewDto findByWine(UUID wineId) {
        Stock stock = stockService.findByWine(wineId);
        return stock == null ? null : stockMapper.mapToDetailViewDto(stock);
    }

    public StockDetailedViewDto addToStock(UUID id, int quantityToAdd) {
        return stockMapper.mapToDetailViewDto(stockService.addToStock(id, quantityToAdd));
    }

    public StockDetailedViewDto removeFromStock(UUID id, int quantityToRemove) {
        return stockMapper.mapToDetailViewDto(stockService.removeFromStock(id, quantityToRemove));
    }

    public List<StockDetailedViewDto> findAll() {
        return stockMapper.mapToList(stockService.findAll());
    }

    public StockDetailedViewDto update(StockDetailedViewDto stock) {
        return stockMapper.mapToDetailViewDto(stockService.update(stockMapper.mapToEntity(stock)));
    }

    public boolean delete(UUID id) {
        return stockService.delete(id);
    }
}
