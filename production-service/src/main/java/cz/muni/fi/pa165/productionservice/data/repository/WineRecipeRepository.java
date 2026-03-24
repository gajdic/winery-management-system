package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WineRecipeRepository {
    WineRecipe save(WineRecipe wineRecipe);

    Optional<WineRecipe> findById(Long id);

    List<WineRecipe> findAll();
}
