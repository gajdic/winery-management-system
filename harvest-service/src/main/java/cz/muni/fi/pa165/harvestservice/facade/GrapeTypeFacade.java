package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.harvestservice.service.GrapeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GrapeTypeFacade {

    private final GrapeTypeService grapeTypeService;

    @Autowired
    public GrapeTypeFacade(GrapeTypeService grapeTypeService) {
        this.grapeTypeService = grapeTypeService;
    }

    public GrapeType create(GrapeType grapeType) {
        return grapeTypeService.create(grapeType);
    }

    public GrapeType findById(UUID id) {
        return grapeTypeService.findById(id);
    }

    public GrapeType findByName(String name) {
        return grapeTypeService.findByName(name);
    }

    public List<GrapeType> findAll() {
        return grapeTypeService.findAll();
    }

    public void deleteById(UUID id) {
        grapeTypeService.deleteById(id);
    }

}
