package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VineyardService {

    private final VineyardRepository vineyardRepository;

    @Autowired
    public VineyardService(VineyardRepository vineyardRepository) {
        this.vineyardRepository = vineyardRepository;
    }

    public Vineyard create(Vineyard vineyard) {
        if (vineyard == null) {
            throw new IllegalArgumentException("Vineyard cannot be null.");
        }

        if (vineyard.getId() == null) {
            throw new IllegalArgumentException("Vineyard id cannot be null.");
        }

        if (vineyard.getName() == null || vineyard.getName().isBlank()) {
            throw new IllegalArgumentException("Vineyard name cannot be null or blank.");
        }

        if (vineyard.getLocation() == null || vineyard.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location cannot be null or blank.");
        }

        if (vineyard.getAreaSize() <= 0) {
            throw new IllegalArgumentException("Area size must be greater than 0.");
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

    public Vineyard update(Vineyard vineyard) {
        if (vineyard == null) {
            throw new IllegalArgumentException("Vineyard cannot be null.");
        }

        if (vineyard.getId() == null) {
            throw new IllegalArgumentException("Vineyard id cannot be null.");
        }

        if  (vineyard.getName() == null || vineyard.getName().isBlank()) {
            throw new IllegalArgumentException("Vineyard name cannot be null or blank.");
        }

        if (vineyard.getLocation() == null || vineyard.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location cannot be null or blank.");
        }

        if (vineyard.getAreaSize() <= 0) {
            throw new IllegalArgumentException("Area size must be greater than 0.");
        }

        try {
            return vineyardRepository.update(vineyard);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(
                "Vineyard with id: " + vineyard.getId() + " was not found."
            );
        }
    }

    public void deleteById(UUID id) {
        vineyardRepository.deleteById(id);
    }
}
