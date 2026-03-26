package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGrapeTypeRepository implements GrapeTypeRepository {

    private final ConcurrentHashMap<UUID, GrapeType> storage = new ConcurrentHashMap<>();

    @Override
    public GrapeType save(GrapeType grapeType) {
        if (grapeType == null) {
            throw new IllegalArgumentException("GrapeType cannot be null");
        }

        if (grapeType.getId() == null) {
            grapeType.setId(UUID.randomUUID());
        }

        storage.put(grapeType.getId(), grapeType);
        return grapeType;
    }

    @Override
    public Optional<GrapeType> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<GrapeType> findByName(String name) {
        return storage.values().stream()
            .filter(gt -> gt.getName() != null && gt.getName().equalsIgnoreCase(name))
            .findFirst();
        }

    @Override
    public List<GrapeType> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public GrapeType update(GrapeType grapeType) {
        if (grapeType == null) {
            throw new IllegalArgumentException("Grape type cannot be null");
        }

        if (grapeType.getId() == null || !storage.containsKey(grapeType.getId())) {
            throw new IllegalArgumentException("Grape type with id " + grapeType.getId() + " does not exist");
        }

        storage.put(grapeType.getId(), grapeType);
        return grapeType;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
