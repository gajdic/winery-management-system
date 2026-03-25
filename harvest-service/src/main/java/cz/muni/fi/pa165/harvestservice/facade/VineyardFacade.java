package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.mappers.VineyardMapper;
import cz.muni.fi.pa165.harvestservice.service.VineyardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VineyardFacade {

    private final VineyardService vineyardService;
    private final VineyardMapper vineyardMapper;

    @Autowired
    public VineyardFacade(VineyardService vineyardService, VineyardMapper vineyardMapper) {
        this.vineyardService = vineyardService;
        this.vineyardMapper = vineyardMapper;
    }

    public VineyardViewDto create(VineyardCreateDto dto) {
        Vineyard vineyard = vineyardMapper.mapToEntity(dto);
        Vineyard createdVineyard = vineyardService.create(vineyard);
        return vineyardMapper.mapToDto(createdVineyard);
    }

    public VineyardViewDto findById(UUID id) {
        return vineyardMapper.mapToDto(vineyardService.findById(id));
    }

    public VineyardViewDto findByName(String name) {
        return vineyardMapper.mapToDto(vineyardService.findByName(name));
     }

    public List<VineyardViewDto> findAll() {
        return vineyardMapper.mapToList(vineyardService.findAll());
    }

    public VineyardViewDto update(UUID id, VineyardUpdateDto dto) {
        Vineyard existingVineyard = vineyardService.findById(id);
        vineyardMapper.updateEntity(dto, existingVineyard);
        Vineyard updated = vineyardService.update(existingVineyard);
        return vineyardMapper.mapToDto(updated);
    }

    public void deleteById(UUID id) {
        vineyardService.deleteById(id);
    }
}
