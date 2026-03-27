package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VineyardRepository {

    Vineyard save(Vineyard vineyard);

    Optional<Vineyard> findById(UUID id);

    Optional<Vineyard> findByName(String name);

    List<Vineyard> findAll();

    Vineyard update(Vineyard vineyard);

    void deleteById(UUID id);
}
