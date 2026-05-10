package cz.muni.fi.pa165.harvestservice.integration;

import cz.muni.fi.pa165.harvestservice.HarvestServiceApplication;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.HarvestRecordRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = HarvestServiceApplication.class)
@ActiveProfiles("seed")
class HarvestSeedIT {

    @Autowired
    private GrapeTypeRepository grapeTypeRepository;

    @Autowired
    private VineyardRepository vineyardRepository;

    @Autowired
    private HarvestRecordRepository harvestRecordRepository;

    @Test
    void seedProfile_loadsRealisticHarvestData() {
        assertThat(grapeTypeRepository.count()).isGreaterThanOrEqualTo(4);
        assertThat(vineyardRepository.count()).isGreaterThanOrEqualTo(4);
        assertThat(harvestRecordRepository.count()).isGreaterThanOrEqualTo(6);

        assertThat(grapeTypeRepository.findByName("Riesling")).isPresent();
        assertThat(vineyardRepository.findByName("Mikulov Hills")).isPresent();
        assertThat(harvestRecordRepository.findByYear(2025)).hasSizeGreaterThanOrEqualTo(3);
    }
}
