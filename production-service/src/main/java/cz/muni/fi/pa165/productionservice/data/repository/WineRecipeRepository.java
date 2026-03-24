package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WineRecipeRepository {
    WineRecipe save(WineRecipe wineRecipe);

    Optional<WineRecipe> findById(UUID id);

    List<WineRecipe> findAll();

    Optional<WineRecipe> deleteById(UUID id);
}
