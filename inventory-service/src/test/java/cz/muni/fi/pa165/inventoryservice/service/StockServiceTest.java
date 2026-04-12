package cz.muni.fi.pa165.inventoryservice.service;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.data.repository.StockRepository;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void findById_stockFound_returnsStock() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity();
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.of(stock));

        // Act
        Stock foundStock = stockService.findById(TestDataFactory.STOCK_ID);

        // Assert
        assertThat(foundStock).isEqualTo(stock);
    }

    @Test
    void findById_stockNotFound_returnsNull() {
        // Arrange
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.empty());

        // Act
        Stock foundStock = stockService.findById(TestDataFactory.STOCK_ID);

        // Assert
        assertThat(foundStock).isNull();
    }

    @Test
    void findByWine_stockFound_returnsStock() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity();
        Mockito.when(stockRepository.findByWineId(TestDataFactory.WINE_ID)).thenReturn(Optional.of(stock));

        // Act
        Stock foundStock = stockService.findByWine(TestDataFactory.WINE_ID);

        // Assert
        assertThat(foundStock).isEqualTo(stock);
    }

    @Test
    void addToStock_positiveQuantity_returnsUpdatedStock() {
        // Arrange
        Stock updatedStock = TestDataFactory.stockEntity(130, 20);
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.of(TestDataFactory.stockEntity(120, 20)));
        Mockito.when(stockRepository.save(updatedStock)).thenReturn(updatedStock);

        // Act
        Stock result = stockService.addToStock(TestDataFactory.STOCK_ID, 10);

        // Assert
        assertThat(result).isEqualTo(updatedStock);
    }

    @Test
    void addToStock_zeroQuantity_throwsIllegalArgumentException() {
        // Act + Assert
        assertThatThrownBy(() -> stockService.addToStock(TestDataFactory.STOCK_ID, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    void removeFromStock_sufficientAvailableQuantity_returnsUpdatedStock() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity(120, 20);
        Stock updatedStock = TestDataFactory.stockEntity(110, 20);
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.of(stock));
        Mockito.when(stockRepository.save(updatedStock)).thenReturn(updatedStock);

        // Act
        Stock result = stockService.removeFromStock(TestDataFactory.STOCK_ID, 10);

        // Assert
        assertThat(result).isEqualTo(updatedStock);
        Mockito.verify(stockRepository).findById(TestDataFactory.STOCK_ID);
    }

    @Test
    void removeFromStock_insufficientAvailableQuantity_throwsIllegalArgumentException() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity(20, 15);
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.of(stock));

        // Act + Assert
        assertThatThrownBy(() -> stockService.removeFromStock(TestDataFactory.STOCK_ID, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cannot remove more bottles than available stock");
        Mockito.verify(stockRepository).findById(TestDataFactory.STOCK_ID);
    }

    @Test
    void removeFromStock_stockNotFound_throwsIllegalArgumentException() {
        // Arrange
        Mockito.when(stockRepository.findById(TestDataFactory.STOCK_ID)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> stockService.removeFromStock(TestDataFactory.STOCK_ID, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Stock not found: " + TestDataFactory.STOCK_ID);
        Mockito.verify(stockRepository).findById(TestDataFactory.STOCK_ID);
    }

    @Test
    void findAll_stocksExist_returnsStocks() {
        // Arrange
        List<Stock> stocks = List.of(TestDataFactory.stockEntity());
        Mockito.when(stockRepository.findAll()).thenReturn(stocks);

        // Act
        List<Stock> foundStocks = stockService.findAll();

        // Assert
        assertThat(foundStocks).isEqualTo(stocks);
    }
}
