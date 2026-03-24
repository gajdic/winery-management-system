package cz.muni.fi.pa165.productionservice.data.repository;

import cz.muni.fi.pa165.productionservice.data.model.WineRecipe;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryWineRecipeRepository implements WineRecipeRepository {
    private final AtomicLong idSequence = new AtomicLong(1);
    private final ConcurrentMap<Long, WineRecipe> recipes = new ConcurrentHashMap<>();

    @Override
    public WineRecipe save(WineRecipe wineRecipe) {
        if (wineRecipe.getId() == null) {
            wineRecipe.setId(idSequence.getAndIncrement());
        }
        recipes.put(wineRecipe.getId(), wineRecipe);
        return wineRecipe;
    }

    @Override
    public Optional<WineRecipe> findById(Long id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public List<WineRecipe> findAll() {
        return new ArrayList<>(recipes.values());
    }
}

