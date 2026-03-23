package cz.muni.fi.pa165.inventoryservice.controller;

import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.facade.StockFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stocks")
public class StockController {

	private final StockFacade stockFacade;

    @Autowired
	public StockController(StockFacade stockFacade) {
		this.stockFacade = stockFacade;
	}

	@PostMapping
	public ResponseEntity<StockDetailedViewDto> create(@RequestBody Stock stock) {
		StockDetailedViewDto created = stockFacade.create(stock);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StockDetailedViewDto> findById(@PathVariable UUID id) {
		StockDetailedViewDto stock = stockFacade.findById(id);
		if (stock == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(stock);
	}

	@GetMapping
	public ResponseEntity<List<StockDetailedViewDto>> findAll() {
		return ResponseEntity.ok(stockFacade.findAll());
	}

	@PutMapping("/{id}")
	public ResponseEntity<StockDetailedViewDto> update(@PathVariable UUID id, @RequestBody Stock stock) {
		stock.setId(id);
		StockDetailedViewDto updated = stockFacade.update(stock);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		boolean deleted = stockFacade.delete(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

}
