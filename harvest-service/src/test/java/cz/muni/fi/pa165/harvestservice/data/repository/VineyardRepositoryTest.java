package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VineyardRepositoryTest {

    @Autowired
    private VineyardRepository vineyardRepository;

    @Test
    void findByName_existingName_returnsMatchingVineyard() {
        Vineyard sonberk = new Vineyard();
        sonberk.setName("Sonberk");
        sonberk.setLocation("Popice");
        sonberk.setAreaSize(45.0);
        sonberk.setDescription("South Moravian vineyard");
        vineyardRepository.saveAndFlush(sonberk);

        Vineyard dobra = new Vineyard();
        dobra.setName("Dobra Vinice");
        dobra.setLocation("Nova Ves");
        dobra.setAreaSize(12.0);
        dobra.setDescription("Organic vineyard");
        vineyardRepository.saveAndFlush(dobra);

        Optional<Vineyard> result = vineyardRepository.findByName("Sonberk");

        assertThat(result).isPresent();
        assertThat(result.get().getLocation()).isEqualTo("Popice");
        assertThat(result.get().getAreaSize()).isEqualTo(45.0);
    }

    @Test
    void findByLocationIgnoreCase_matchingLocationReturnsAllMatches() {
        Vineyard first = new Vineyard();
        first.setName("Vineyard One");
        first.setLocation("Mikulov");
        first.setAreaSize(10.5);
        first.setDescription("First vineyard");
        vineyardRepository.saveAndFlush(first);

        Vineyard second = new Vineyard();
        second.setName("Vineyard Two");
        second.setLocation("MIKULOV");
        second.setAreaSize(8.0);
        second.setDescription("Second vineyard");
        vineyardRepository.saveAndFlush(second);

        Vineyard third = new Vineyard();
        third.setName("Vineyard Three");
        third.setLocation("Znojmo");
        third.setAreaSize(6.0);
        third.setDescription("Third vineyard");
        vineyardRepository.saveAndFlush(third);

        List<Vineyard> result = vineyardRepository.findByLocationIgnoreCase("mikulov");

        assertThat(result)
            .hasSize(2)
            .extracting(Vineyard::getName)
            .containsExactlyInAnyOrder("Vineyard One", "Vineyard Two");
    }
}
