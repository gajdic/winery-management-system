package cz.muni.fi.pa165.inventoryservice.data.repository;

import cz.muni.fi.pa165.inventoryservice.data.models.EntityBase;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;

import java.time.LocalDate;
import java.util.*;

public class InMemoryRepository<T extends EntityBase> {

    protected Map<UUID, T> storage = new HashMap<>();

    public T create(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T update(T entity) {
        if (entity.getId() != null && storage.containsKey(entity.getId())) {
            storage.put(entity.getId(), entity);
            return entity;
        }
        throw new IllegalArgumentException("Entity with ID " + entity.getId() + " does not exist");
    }

    public boolean delete(UUID id) {
        return storage.remove(id) != null;
    }
}

