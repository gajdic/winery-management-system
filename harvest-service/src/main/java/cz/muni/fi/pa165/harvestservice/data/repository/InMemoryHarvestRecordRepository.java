package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHarvestRecordRepository implements HarvestRecordRepository {

    private final ConcurrentHashMap<UUID, HarvestRecord> storage = new ConcurrentHashMap<>();

    @Override
    public HarvestRecord save(HarvestRecord harvestRecord) {
        if (harvestRecord == null) {
            throw new IllegalArgumentException("HarvestRecord cannot be null");
        }
        if (harvestRecord.getId() == null) {
            harvestRecord.setId(UUID.randomUUID());
        }

        storage.put(harvestRecord.getId(), harvestRecord);
        return harvestRecord;
    }

    @Override
    public Optional<HarvestRecord> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<HarvestRecord> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<HarvestRecord> findByVineyardId(UUID vineyardId) {
        return storage.values().stream()
            .filter(record -> record.getVineyard() != null && record.getVineyard().getId() != null)
            .filter(record -> record.getVineyard().getId().equals(vineyardId))
            .toList();
    }

    @Override
    public List<HarvestRecord> findByGrapeTypeId(UUID grapeTypeId) {
        return storage.values().stream()
            .filter(record -> record.getGrapeType() != null && record.getGrapeType().getId() != null)
            .filter(record -> record.getGrapeType().getId().equals(grapeTypeId))
            .toList();
    }

    @Override
    public List<HarvestRecord> findByYear(int year) {
        return storage.values().stream()
            .filter(record -> record.getHarvestDate() != null && record.getHarvestDate().getYear() == year)
            .toList();
    }

    @Override
    public HarvestRecord update(HarvestRecord harvestRecord) {
        if (harvestRecord == null) {
            throw new IllegalArgumentException("HarvestRecord cannot be null");
        }

        if (harvestRecord.getId() == null || !storage.containsKey(harvestRecord.getId())) {
            throw new IllegalArgumentException("HarvestRecord with id " + harvestRecord.getId() + " does not exist");
        }

        storage.put(harvestRecord.getId(), harvestRecord);
        return harvestRecord;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
