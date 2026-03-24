package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VineyardService {

    private final VineyardRepository vineyardRepository;

    public VineyardService(VineyardRepository vineyardRepository) {
        this.vineyardRepository = vineyardRepository;
    }

    public Vineyard create(Vineyard vineyard) {
        if (vineyard == null) {
            throw new IllegalArgumentException("Vineyard cannot be null.");
        }

        if (vineyard.getName() == null || vineyard.getName().isBlank()) {
            throw new IllegalArgumentException("Vineyard name cannot be null or blank.");
        }

        return vineyardRepository.save(vineyard);
    }

    public Vineyard findById(UUID id) {
        return vineyardRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vineyard with id: " + id + " was not found."));
    }

    public Vineyard findByName(String name) {
        return vineyardRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Vineyard with name: " + name + " was not found."));
    }

    public List<Vineyard> findAll() {
        return vineyardRepository.findAll();
    }

    public Vineyard update(UUID id, String name, String location, double areaSize, String description) {
        Vineyard vineyard = findById(id);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Vineyard name cannot be null or blank.");
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be null or blank.");
        }

        if (areaSize <= 0) {
            throw new IllegalArgumentException("Area size must be greater than 0.");
        }

        vineyard.setName(name);
        vineyard.setLocation(location);
        vineyard.setAreaSize(areaSize);
        vineyard.setDescription(description);

        return vineyardRepository.save(vineyard);
    }

    public void deleteById(UUID id) {
        vineyardRepository.deleteById(id);
    }
}
