package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HarvestRecordRepositoryTest {

    @Autowired
    private HarvestRecordRepository harvestRecordRepository;

    @Autowired
    private GrapeTypeRepository grapeTypeRepository;

    @Autowired
    private VineyardRepository vineyardRepository;

    @Test
    void findByVineyardId_existingVineyard_returnsMatchingRecords() {
        // Arrange
        Vineyard matchingVineyard = vineyardRepository.saveAndFlush(TestDataFactory.vineyardEntity("Popice Vineyard", "Popice"));
        Vineyard otherVineyard = vineyardRepository.saveAndFlush(TestDataFactory.vineyardEntity("Znojmo Vineyard", "Znojmo"));
        GrapeType grapeType = grapeTypeRepository.saveAndFlush(TestDataFactory.grapeTypeEntity("Welschriesling"));

        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, matchingVineyard, LocalDate.of(2024, 9, 15)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, matchingVineyard, LocalDate.of(2025, 9, 18)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, otherVineyard, LocalDate.of(2024, 10, 1)));

        // Act
        List<HarvestRecord> result = harvestRecordRepository.findByVineyardId(matchingVineyard.getId());

        // Assert
        assertThat(result)
            .hasSize(2)
            .extracting(HarvestRecord::getVineyard)
            .containsOnly(matchingVineyard);
    }

    @Test
    void findByGrapeTypeId_existingGrapeType_returnsMatchingRecords() {
        // Arrange
        Vineyard vineyard = vineyardRepository.saveAndFlush(TestDataFactory.vineyardEntity("Lednice Vineyard", "Lednice"));
        GrapeType matchingType = grapeTypeRepository.saveAndFlush(TestDataFactory.grapeTypeEntity("Riesling"));
        GrapeType otherType = grapeTypeRepository.saveAndFlush(TestDataFactory.grapeTypeEntity("Pinot Noir"));

        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(matchingType, vineyard, LocalDate.of(2024, 9, 10)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(matchingType, vineyard, LocalDate.of(2025, 9, 11)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(otherType, vineyard, LocalDate.of(2024, 9, 12)));

        // Act
        List<HarvestRecord> result = harvestRecordRepository.findByGrapeTypeId(matchingType.getId());

        // Assert
        assertThat(result)
            .hasSize(2)
            .extracting(record -> record.getGrapeType().getName())
            .containsOnly("Riesling");
    }

    @Test
    void findByYear_existingYear_returnsMatchingRecords() {
        // Arrange
        Vineyard vineyard = vineyardRepository.saveAndFlush(TestDataFactory.vineyardEntity("Valtice Vineyard", "Valtice"));
        GrapeType grapeType = grapeTypeRepository.saveAndFlush(TestDataFactory.grapeTypeEntity("Palava"));

        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, vineyard, LocalDate.of(2024, 9, 20)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, vineyard, LocalDate.of(2024, 10, 2)));
        harvestRecordRepository.saveAndFlush(TestDataFactory.harvestRecordEntity(grapeType, vineyard, LocalDate.of(2025, 9, 25)));

        // Act
        List<HarvestRecord> result = harvestRecordRepository.findByYear(2024);

        // Assert
        assertThat(result)
            .hasSize(2)
            .extracting(HarvestRecord::getHarvestDate)
            .allMatch(date -> date.getYear() == 2024);
    }
}
