package cz.muni.fi.pa165.harvestservice.rest;

import cz.muni.fi.pa165.commonservice.api.harvest.VineyardCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardUpdateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.facade.VineyardFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/vineyards")
public class VineyardRestController {

    private final VineyardFacade vineyardFacade;

    @Autowired
    public VineyardRestController(VineyardFacade vineyardFacade) {
        this.vineyardFacade = vineyardFacade;
    }

    @PostMapping
    public ResponseEntity<VineyardViewDto> create(@RequestBody @Valid VineyardCreateDto dto) {
        return ResponseEntity.status(201).body(vineyardFacade.create(dto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<VineyardViewDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(vineyardFacade.findById(id));
    }

    @GetMapping(path = "/by-name/{name}")
    public ResponseEntity<VineyardViewDto> findByName(@PathVariable String name) {
        return  ResponseEntity.ok(vineyardFacade.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<VineyardViewDto>> findAll() {
        return ResponseEntity.ok(vineyardFacade.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VineyardViewDto> update(@PathVariable UUID id, @RequestBody @Valid VineyardUpdateDto dto) {
        return ResponseEntity.ok(vineyardFacade.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vineyardFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

