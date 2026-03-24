package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GrapeTypeRepository {

    GrapeType save(GrapeType grapeType);

    Optional<GrapeType> findById(UUID id);

    Optional<GrapeType> findByName(String name);

    List<GrapeType> findAll();

    void deleteById(UUID id);
}
