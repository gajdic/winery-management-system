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

    public GrapeTypeService(GrapeTypeRepository grapeTypeRepository) {
        this.grapeTypeRepository = grapeTypeRepository;
    }

    public GrapeType create(GrapeType grapeType) {
        if (grapeType == null) {
            throw new IllegalArgumentException("GrapeType cannot be null");
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

    public GrapeType update(UUID id, String name, String description) {
        GrapeType existing = grapeTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "GrapeType with id: " + id + " was not found."
            ));

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("GrapeType name cannot be null or blank");
        }

        existing.setName(name);
        existing.setDescription(description);

        return grapeTypeRepository.save(existing);
    }

    public void deleteById(UUID id) {
        grapeTypeRepository.deleteById(id);
    }
}
