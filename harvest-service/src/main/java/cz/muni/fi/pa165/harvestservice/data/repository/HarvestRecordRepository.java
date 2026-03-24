package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestRecordRepository {

    HarvestRecord save(HarvestRecord harvestRecord);

    Optional<HarvestRecord> findById(UUID id);

    List<HarvestRecord> findAll();

    List<HarvestRecord> findByVineyardId(UUID vineyardId);

    List<HarvestRecord> findByGrapeTypeId(UUID grapeTypeId);

    List<HarvestRecord> findByYear(int year);

    void deleteById(UUID id);
}
