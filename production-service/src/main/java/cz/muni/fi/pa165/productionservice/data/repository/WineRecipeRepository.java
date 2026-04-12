package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WineRecipeRepository extends JpaRepository<WineRecipe, UUID> {
    @Override
    @EntityGraph(attributePaths = {"grapeComponents", "additionalIngredients"})
    Optional<WineRecipe> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"grapeComponents", "additionalIngredients"})
    List<WineRecipe> findAll();

    List<WineRecipe> findByNameContainingIgnoreCase(String name);

    List<WineRecipe> findByProductionYear(int productionYear);
}
