package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GrapeTypeService {
    private final GrapeTypeRepository grapeTypeRepository;

    @Autowired
    public GrapeTypeService(GrapeTypeRepository grapeTypeRepository) {
        this.grapeTypeRepository = grapeTypeRepository;
    }

    public GrapeType create(GrapeType grapeType) {
        if (grapeType == null) {
            throw new IllegalArgumentException("Grape type cannot be null");
        }

        if  (grapeType.getName() == null || grapeType.getName().isBlank()) {
            throw new IllegalArgumentException("Grape type name cannot be null or blank");
        }

        return grapeTypeRepository.save(grapeType);
    }

    public GrapeType findById(UUID id) {
        return grapeTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grape type with id: " + id + " was not found."));
    }

    public GrapeType findByName(String name) {
        return grapeTypeRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Grape type with name: " + name + " was not found."));
    }

    public List<GrapeType> findAll() {
        return grapeTypeRepository.findAll();
    }

    public GrapeType update(GrapeType grapeType) {
        if  (grapeType == null) {
            throw new IllegalArgumentException("Grape type cannot be null");
        }

        if (grapeType.getId() == null) {
            throw new IllegalArgumentException("Grape type id cannot be null");
        }

        if  (grapeType.getName() == null || grapeType.getName().isBlank()) {
            throw new IllegalArgumentException("Grape type name cannot be null or blank");
        }

        GrapeType existing = grapeTypeRepository.findById(grapeType.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grape type with id: " + grapeType.getId() + " was not found."
            ));

        existing.setName(grapeType.getName());
        existing.setDescription(grapeType.getDescription());

        return grapeTypeRepository.save(existing);
    }

    public void deleteById(UUID id) {
        grapeTypeRepository.deleteById(id);
    }
}
