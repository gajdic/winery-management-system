package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.mappers.GrapeTypeMapper;
import cz.muni.fi.pa165.harvestservice.service.GrapeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GrapeTypeFacade {

    private final GrapeTypeService grapeTypeService;
    private final GrapeTypeMapper grapeTypeMapper;

    @Autowired
    public GrapeTypeFacade(GrapeTypeService grapeTypeService, GrapeTypeMapper grapeTypeMapper) {
        this.grapeTypeService = grapeTypeService;
        this.grapeTypeMapper = grapeTypeMapper;
    }

    public GrapeTypeViewDto create(GrapeTypeCreateDto dto) {
        GrapeType grapeType = grapeTypeMapper.mapToEntity(dto);
        GrapeType createdGrapeType = grapeTypeService.create(grapeType);
        return grapeTypeMapper.mapToDto(createdGrapeType);
    }

    public GrapeTypeViewDto findById(UUID id) {
        return grapeTypeMapper.mapToDto(grapeTypeService.findById(id));
    }

    public GrapeTypeViewDto findByName(String name) {
        return grapeTypeMapper.mapToDto(grapeTypeService.findByName(name));
    }

    public List<GrapeTypeViewDto> findAll() {
        return grapeTypeMapper.mapToList(grapeTypeService.findAll());
    }

    public GrapeTypeViewDto update(UUID id, GrapeTypeUpdateDto dto) {
        GrapeType grapeType = grapeTypeMapper.mapToEntity(id, dto);
        GrapeType updated = grapeTypeService.update(grapeType);
        return grapeTypeMapper.mapToDto(updated);
    }

    public void deleteById(UUID id) {
        grapeTypeService.deleteById(id);
    }

}
