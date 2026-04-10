package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VineyardRepository extends JpaRepository<Vineyard, UUID>, QueryByExampleExecutor<Vineyard> {

    Optional<Vineyard> findByName(String name);

    List<Vineyard> findByLocationIgnoreCase(String location);
}
