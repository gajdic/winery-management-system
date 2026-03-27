package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryVineyardRepository implements VineyardRepository {

    private final Map<UUID, Vineyard> storage = new ConcurrentHashMap<>();

    @Override
    public Vineyard save(Vineyard vineyard) {
        if (vineyard == null) {
            throw new IllegalArgumentException("Vineyard cannot be null");
        }

        if (vineyard.getId() == null) {
            vineyard.setId(UUID.randomUUID());
        }

        storage.put(vineyard.getId(), vineyard);
        return vineyard;
    }

    @Override
    public Optional<Vineyard> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Vineyard> findByName(String name) {
        return storage.values().stream()
            .filter(v -> v.getName() != null && v.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    @Override
    public List<Vineyard> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Vineyard update(Vineyard vineyard) {
        if (vineyard == null) {
            throw new IllegalArgumentException("Vineyard cannot be null");
        }

        if (vineyard.getId() == null || !storage.containsKey(vineyard.getId())) {
            throw new IllegalArgumentException("Vineyard with id " + vineyard.getId() + " does not exist");
        }

        storage.put(vineyard.getId(), vineyard);
        return vineyard;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
