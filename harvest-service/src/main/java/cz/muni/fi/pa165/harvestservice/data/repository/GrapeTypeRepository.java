package cz.muni.fi.pa165.harvestservice.data.repository;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrapeTypeRepository extends JpaRepository<GrapeType, UUID>, QueryByExampleExecutor<GrapeType> {

    Optional<GrapeType> findByName(String name);

    boolean existsByName(String name);

}
