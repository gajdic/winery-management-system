package cz.muni.fi.pa165.inventoryservice.facade;

import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.mappers.StockMapper;
import cz.muni.fi.pa165.inventoryservice.service.StockService;
import cz.muni.fi.pa165.inventoryservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StockFacadeTest {

    @Mock
    private StockService stockService;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockFacade stockFacade;

    @Test
    void findById_stockFound_returnsStockDetailedViewDto() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity();
        StockDetailedViewDto dto = TestDataFactory.stockDetailedViewDto();
        Mockito.when(stockService.findById(TestDataFactory.STOCK_ID)).thenReturn(stock);
        Mockito.when(stockMapper.mapToDetailViewDto(stock)).thenReturn(dto);

        // Act
        StockDetailedViewDto result = stockFacade.findById(TestDataFactory.STOCK_ID);

        // Assert
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_stockNotFound_returnsNull() {
        // Arrange
        Mockito.when(stockService.findById(TestDataFactory.STOCK_ID)).thenReturn(null);

        // Act
        StockDetailedViewDto result = stockFacade.findById(TestDataFactory.STOCK_ID);

        // Assert
        assertThat(result).isNull();
        Mockito.verify(stockMapper, Mockito.never()).mapToDetailViewDto(Mockito.any());
    }

    @Test
    void create_validStock_returnsCreatedStockDetailedViewDto() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity();
        StockDetailedViewDto dto = TestDataFactory.stockDetailedViewDto();
        Mockito.when(stockMapper.mapToEntity(dto)).thenReturn(stock);
        Mockito.when(stockService.create(stock)).thenReturn(stock);
        Mockito.when(stockMapper.mapToDetailViewDto(stock)).thenReturn(dto);

        // Act
        StockDetailedViewDto result = stockFacade.create(dto);

        // Assert
        assertThat(result).isEqualTo(dto);
        Mockito.verify(stockMapper).mapToEntity(dto);
        Mockito.verify(stockService).create(stock);
    }

    @Test
    void addToStock_validQuantity_returnsUpdatedStockDetailedViewDto() {
        // Arrange
        Stock stock = TestDataFactory.stockEntity(130, 20);
        StockDetailedViewDto dto = TestDataFactory.stockDetailedViewDto(130, 20);
        Mockito.when(stockService.addToStock(TestDataFactory.STOCK_ID, 10)).thenReturn(stock);
        Mockito.when(stockMapper.mapToDetailViewDto(stock)).thenReturn(dto);

        // Act
        StockDetailedViewDto result = stockFacade.addToStock(TestDataFactory.STOCK_ID, 10);

        // Assert
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findAll_stocksExist_returnsStockDetailedViewDtos() {
        // Arrange
        List<Stock> stocks = List.of(TestDataFactory.stockEntity());
        List<StockDetailedViewDto> dtos = TestDataFactory.stockDetailedViewDtos();
        Mockito.when(stockService.findAll()).thenReturn(stocks);
        Mockito.when(stockMapper.mapToList(stocks)).thenReturn(dtos);

        // Act
        List<StockDetailedViewDto> result = stockFacade.findAll();

        // Assert
        assertThat(result).isEqualTo(dtos);
    }
}
