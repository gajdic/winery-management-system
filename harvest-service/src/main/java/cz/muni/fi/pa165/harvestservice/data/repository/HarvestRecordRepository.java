package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.UUID;

public interface HarvestRecordRepository extends JpaRepository<HarvestRecord, UUID>, QueryByExampleExecutor<HarvestRecord> {

    List<HarvestRecord> findByVineyardId(UUID vineyardId);

    List<HarvestRecord> findByGrapeTypeId(UUID grapeTypeId);

    @Query("select h from HarvestRecord h where year(h.harvestDate) = :year")
    List<HarvestRecord> findByYear(@Param("year") int year);
}
