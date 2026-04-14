package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private UUID wineId1;
    private UUID wineId2;

    @BeforeEach
    void initData() {
        wineId1 = UUID.randomUUID();
        wineId2 = UUID.randomUUID();

        Stock stock1 = new Stock();
        stock1.setWineId(wineId1);
        stock1.setQuantity(100);
        stock1.setReservedQuantity(20);
        testEntityManager.persist(stock1);

        Stock stock2 = new Stock();
        stock2.setWineId(wineId2);
        stock2.setQuantity(50);
        stock2.setReservedQuantity(50);
        testEntityManager.persist(stock2);

        testEntityManager.flush();
    }

    @Test
    void findByWineId_stockFound_returnsStock() {
        // Act
        Optional<Stock> found = stockRepository.findByWineId(wineId1);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getWineId()).isEqualTo(wineId1);
        assertThat(found.get().getQuantity()).isEqualTo(100);
        assertThat(found.get().getReservedQuantity()).isEqualTo(20);
    }

    @Test
    void findByWineId_stockNotFound_returnsEmpty() {
        // Act
        Optional<Stock> found = stockRepository.findByWineId(UUID.randomUUID());

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void save_newStock_persistsCorrectly() {
        // Arrange
        UUID newWineId = UUID.randomUUID();
        Stock stock = new Stock();
        stock.setWineId(newWineId);
        stock.setQuantity(75);
        stock.setReservedQuantity(5);

        // Act
        Stock saved = stockRepository.save(stock);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getWineId()).isEqualTo(newWineId);
        assertThat(saved.getQuantity()).isEqualTo(75);
        assertThat(saved.getReservedQuantity()).isEqualTo(5);
    }

    @Test
    void findAll_returnsAllStocks() {
        // Act
        List<Stock> all = stockRepository.findAll();

        // Assert
        assertThat(all).hasSize(2);
    }

    @Test
    void deleteById_stockDeleted_notFoundAnymore() {
        // Arrange
        Stock stock = stockRepository.findByWineId(wineId1).orElseThrow();

        // Act
        stockRepository.deleteById(stock.getId());
        testEntityManager.flush();

        // Assert
        assertThat(stockRepository.findByWineId(wineId1)).isEmpty();
        assertThat(stockRepository.findAll()).hasSize(1);
    }

    @Test
    void findById_existingStock_returnsStock() {
        // Arrange
        Stock stock = stockRepository.findByWineId(wineId2).orElseThrow();

        // Act
        Optional<Stock> found = stockRepository.findById(stock.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(50);
        assertThat(found.get().getReservedQuantity()).isEqualTo(50);
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // Act
        Optional<Stock> found = stockRepository.findById(UUID.randomUUID());

        // Assert
        assertThat(found).isEmpty();
    }
}
