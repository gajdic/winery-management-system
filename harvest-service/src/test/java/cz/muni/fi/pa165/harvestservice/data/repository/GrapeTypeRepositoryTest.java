package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GrapeTypeRepositoryTest {

    @Autowired
    private GrapeTypeRepository grapeTypeRepository;

    @Test
    void findByName_existingName_returnsMatchingGrapeType() {
        // Arrange
        GrapeType welschriesling = new GrapeType();
        welschriesling.setName("Welschriesling");
        welschriesling.setDescription("Fresh white grape");
        grapeTypeRepository.saveAndFlush(welschriesling);

        GrapeType riesling = new GrapeType();
        riesling.setName("Riesling");
        riesling.setDescription("Aromatic white grape");
        grapeTypeRepository.saveAndFlush(riesling);

        // Act
        Optional<GrapeType> result = grapeTypeRepository.findByName("Welschriesling");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Welschriesling");
        assertThat(result.get().getDescription()).isEqualTo("Fresh white grape");
    }

    @Test
    void existsByName_existingName_returnsTrue() {
        // Arrange
        GrapeType grapeType = new GrapeType();
        grapeType.setName("Pinot Noir");
        grapeType.setDescription("Red grape");
        grapeTypeRepository.saveAndFlush(grapeType);

        // Act
        boolean result = grapeTypeRepository.existsByName("Pinot Noir");

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void existsByName_missingName_returnsFalse() {
        // Arrange
        GrapeType grapeType = new GrapeType();
        grapeType.setName("Pinot Noir");
        grapeType.setDescription("Red grape");
        grapeTypeRepository.saveAndFlush(grapeType);

        // Act
        boolean result = grapeTypeRepository.existsByName("Merlot");

        // Assert
        assertThat(result).isFalse();
    }
}
