package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.facade.GrapeTypeFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/grape-types")
public class GrapeTypeRestController {

    private final GrapeTypeFacade grapeTypeFacade;

    @Autowired
    public GrapeTypeRestController(GrapeTypeFacade grapeTypeFacade) {
        this.grapeTypeFacade = grapeTypeFacade;
    }

    @PostMapping
    public ResponseEntity<GrapeTypeViewDto> create(@RequestBody @Valid GrapeTypeCreateDto dto) {
        return ResponseEntity.status(201).body(grapeTypeFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GrapeTypeViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(grapeTypeFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    public ResponseEntity<GrapeTypeViewDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(grapeTypeFacade.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<GrapeTypeViewDto>> findAll() {
        return ResponseEntity.ok(grapeTypeFacade.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrapeTypeViewDto> update(@PathVariable UUID id, @RequestBody @Valid GrapeTypeUpdateDto dto) {
        return ResponseEntity.ok(grapeTypeFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        grapeTypeFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

