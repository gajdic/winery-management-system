package cz.muni.fi.pa165.harvestservice.facade;

import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestSummaryDto;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.mappers.HarvestRecordMapper;
import cz.muni.fi.pa165.harvestservice.service.HarvestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HarvestRecordFacade {

    private final HarvestRecordService harvestRecordService;
    private final HarvestRecordMapper harvestRecordMapper;

    @Autowired
    public HarvestRecordFacade(HarvestRecordService harvestRecordService, HarvestRecordMapper harvestRecordMapper) {
        this.harvestRecordService = harvestRecordService;
        this.harvestRecordMapper = harvestRecordMapper;
    }

    public HarvestRecordViewDto create(HarvestRecordCreateDto dto) {
        HarvestRecord harvestRecord = harvestRecordMapper.mapToEntity(dto);
        HarvestRecord createdRecord = harvestRecordService.create(harvestRecord);
        return harvestRecordMapper.mapToDto(createdRecord);
    }

    public HarvestRecordViewDto findById(UUID id) {
        return harvestRecordMapper.mapToDto(harvestRecordService.findById(id));
    }

    public List<HarvestRecordViewDto> findByVineyardId(UUID id) {
        return harvestRecordMapper.mapToList(harvestRecordService.findByVineyardId(id));
    }

    public List<HarvestRecordViewDto> findByGrapeTypeId(UUID id) {
        return harvestRecordMapper.mapToList(harvestRecordService.findByGrapeTypeId(id));
    }

    public List<HarvestRecordViewDto> findByYear(int year) {
        return harvestRecordMapper.mapToList(harvestRecordService.findByYear(year));
    }

    public List<HarvestRecordViewDto> findAll() {
        return harvestRecordMapper.mapToList(harvestRecordService.findAll());
    }

    public List<HarvestSummaryDto> summaryByYear() {
        return harvestRecordMapper.mapSummaryToDtoList(harvestRecordService.summaryByYear());
    }

    public List<HarvestSummaryDto> summaryByVineyard() {
        return harvestRecordMapper.mapSummaryToDtoList(harvestRecordService.summaryByVineyard());
    }

    public HarvestRecordViewDto update(UUID id, HarvestRecordUpdateDto dto) {
        HarvestRecord harvestRecord = harvestRecordMapper.mapToEntity(id, dto);
        HarvestRecord updated = harvestRecordService.update(harvestRecord);
        return harvestRecordMapper.mapToDto(updated);
    }

    public void deleteById(UUID id) {
        harvestRecordService.deleteById(id);
    }

}
