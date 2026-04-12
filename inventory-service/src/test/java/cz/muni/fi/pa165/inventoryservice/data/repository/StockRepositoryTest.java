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
    private UUID wineId3;

    @BeforeEach
    void initData() {
        wineId1 = UUID.randomUUID();
        wineId2 = UUID.randomUUID();
        wineId3 = UUID.randomUUID();

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

        Stock stock3 = new Stock();
        stock3.setWineId(wineId3);
        stock3.setQuantity(200);
        stock3.setReservedQuantity(10);
        testEntityManager.persist(stock3);

        testEntityManager.flush();
    }

    @Test
    void findByWineId_stockFound_returnsStock() {
        Optional<Stock> found = stockRepository.findByWineId(wineId1);

        assertThat(found).isPresent();
        assertThat(found.get().getWineId()).isEqualTo(wineId1);
        assertThat(found.get().getQuantity()).isEqualTo(100);
        assertThat(found.get().getReservedQuantity()).isEqualTo(20);
    }

    @Test
    void findByWineId_stockNotFound_returnsEmpty() {
        Optional<Stock> found = stockRepository.findByWineId(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void findAllWithAvailableQuantityAtLeast_returnsMatchingStocks() {
        // stock1: 100 - 20 = 80 available
        // stock2: 50 - 50 = 0 available
        // stock3: 200 - 10 = 190 available
        List<Stock> result = stockRepository.findAllWithAvailableQuantityAtLeast(50);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Stock::getWineId)
                .containsExactlyInAnyOrder(wineId1, wineId3);
    }

    @Test
    void findAllWithAvailableQuantityAtLeast_highThreshold_returnsOnlyLargeStock() {
        List<Stock> result = stockRepository.findAllWithAvailableQuantityAtLeast(100);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWineId()).isEqualTo(wineId3);
    }

    @Test
    void findAllWithAvailableQuantityAtLeast_zeroThreshold_returnsAll() {
        List<Stock> result = stockRepository.findAllWithAvailableQuantityAtLeast(0);

        assertThat(result).hasSize(3);
    }

    @Test
    void findAllWithAvailableQuantityAtLeast_veryHighThreshold_returnsEmpty() {
        List<Stock> result = stockRepository.findAllWithAvailableQuantityAtLeast(500);

        assertThat(result).isEmpty();
    }

    @Test
    void save_newStock_persistsCorrectly() {
        UUID newWineId = UUID.randomUUID();
        Stock stock = new Stock();
        stock.setWineId(newWineId);
        stock.setQuantity(75);
        stock.setReservedQuantity(5);

        Stock saved = stockRepository.save(stock);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getWineId()).isEqualTo(newWineId);
        assertThat(saved.getQuantity()).isEqualTo(75);
        assertThat(saved.getReservedQuantity()).isEqualTo(5);
    }

    @Test
    void findAll_returnsAllStocks() {
        List<Stock> all = stockRepository.findAll();

        assertThat(all).hasSize(3);
    }

    @Test
    void deleteById_stockDeleted_notFoundAnymore() {
        Stock stock = stockRepository.findByWineId(wineId1).orElseThrow();

        stockRepository.deleteById(stock.getId());
        testEntityManager.flush();

        assertThat(stockRepository.findByWineId(wineId1)).isEmpty();
        assertThat(stockRepository.findAll()).hasSize(2);
    }

    @Test
    void findById_existingStock_returnsStock() {
        Stock stock = stockRepository.findByWineId(wineId2).orElseThrow();

        Optional<Stock> found = stockRepository.findById(stock.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(50);
        assertThat(found.get().getReservedQuantity()).isEqualTo(50);
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<Stock> found = stockRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }
}
