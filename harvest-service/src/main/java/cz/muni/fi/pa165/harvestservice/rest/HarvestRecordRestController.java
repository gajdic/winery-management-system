package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestSummaryDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.facade.HarvestRecordFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/harvest-records")
public class HarvestRecordRestController {

    private final HarvestRecordFacade harvestRecordFacade;

    @Autowired
    public HarvestRecordRestController(HarvestRecordFacade harvestRecordFacade) {
        this.harvestRecordFacade = harvestRecordFacade;
    }

    @PostMapping
    public ResponseEntity<HarvestRecordViewDto> create(@RequestBody @Valid HarvestRecordCreateDto dto) {
        return ResponseEntity.status(201).body(harvestRecordFacade.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HarvestRecordViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findById(id));
    }

    @GetMapping("/by-year/{year}")
    public ResponseEntity<List<HarvestRecordViewDto>> findByYear(@PathVariable int year) {
        return ResponseEntity.ok(harvestRecordFacade.findByYear(year));
    }

    @GetMapping("/by-vineyard/{id}")
    public ResponseEntity<List<HarvestRecordViewDto>> findByVineyardId(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findByVineyardId(id));
    }

    @GetMapping("/by-grape-type/{id}")
    public ResponseEntity<List<HarvestRecordViewDto>> findByGrapeTypeId(@PathVariable UUID id) {
        return ResponseEntity.ok(harvestRecordFacade.findByGrapeTypeId(id));
    }

    @GetMapping
    public ResponseEntity<List<HarvestRecordViewDto>> findAll() {
        return ResponseEntity.ok(harvestRecordFacade.findAll());
    }

    @GetMapping("/summary/by-year")
    public ResponseEntity<List<HarvestSummaryDto>> summaryByYear() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByYear());
    }

    @GetMapping("/summary/by-vineyard")
    public ResponseEntity<List<HarvestSummaryDto>> summaryByVineyard() {
        return ResponseEntity.ok(harvestRecordFacade.summaryByVineyard());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HarvestRecordViewDto> update(@PathVariable UUID id, @RequestBody @Valid HarvestRecordUpdateDto dto) {
        return ResponseEntity.ok(harvestRecordFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        harvestRecordFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

