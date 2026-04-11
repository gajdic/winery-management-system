package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
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
        Vineyard matchingVineyard = vineyardRepository.saveAndFlush(createVineyard("Popice Vineyard", "Popice"));
        Vineyard otherVineyard = vineyardRepository.saveAndFlush(createVineyard("Znojmo Vineyard", "Znojmo"));
        GrapeType grapeType = grapeTypeRepository.saveAndFlush(createGrapeType("Welschriesling"));

        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, matchingVineyard, LocalDate.of(2024, 9, 15)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, matchingVineyard, LocalDate.of(2025, 9, 18)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, otherVineyard, LocalDate.of(2024, 10, 1)));

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
        Vineyard vineyard = vineyardRepository.saveAndFlush(createVineyard("Lednice Vineyard", "Lednice"));
        GrapeType matchingType = grapeTypeRepository.saveAndFlush(createGrapeType("Riesling"));
        GrapeType otherType = grapeTypeRepository.saveAndFlush(createGrapeType("Pinot Noir"));

        harvestRecordRepository.saveAndFlush(createHarvestRecord(matchingType, vineyard, LocalDate.of(2024, 9, 10)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(matchingType, vineyard, LocalDate.of(2025, 9, 11)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(otherType, vineyard, LocalDate.of(2024, 9, 12)));

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
        Vineyard vineyard = vineyardRepository.saveAndFlush(createVineyard("Valtice Vineyard", "Valtice"));
        GrapeType grapeType = grapeTypeRepository.saveAndFlush(createGrapeType("Pálava"));

        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, vineyard, LocalDate.of(2024, 9, 20)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, vineyard, LocalDate.of(2024, 10, 2)));
        harvestRecordRepository.saveAndFlush(createHarvestRecord(grapeType, vineyard, LocalDate.of(2025, 9, 25)));

        // Act
        List<HarvestRecord> result = harvestRecordRepository.findByYear(2024);

        // Assert
        assertThat(result)
            .hasSize(2)
            .extracting(HarvestRecord::getHarvestDate)
            .allMatch(date -> date.getYear() == 2024);
    }

    private Vineyard createVineyard(String name, String location) {
        Vineyard vineyard = new Vineyard();
        vineyard.setName(name);
        vineyard.setLocation(location);
        vineyard.setAreaSize(10.0);
        vineyard.setDescription(name + " description");
        return vineyard;
    }

    private GrapeType createGrapeType(String name) {
        GrapeType grapeType = new GrapeType();
        grapeType.setName(name);
        grapeType.setDescription(name + " description");
        return grapeType;
    }

    private HarvestRecord createHarvestRecord(GrapeType grapeType, Vineyard vineyard, LocalDate harvestDate) {
        HarvestRecord harvestRecord = new HarvestRecord();
        harvestRecord.setGrapeType(grapeType);
        harvestRecord.setVineyard(vineyard);
        harvestRecord.setHarvestDate(harvestDate);
        harvestRecord.setQuantity(1200.0);
        harvestRecord.setQuality(8);
        return harvestRecord;
    }
}
