package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.facade.PurchaseRecordFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
public class PurchaseRecordController {

    private final PurchaseRecordFacade purchaseRecordFacade;

    public PurchaseRecordController(PurchaseRecordFacade purchaseRecordFacade) {
        this.purchaseRecordFacade = purchaseRecordFacade;
    }

    @PostMapping
    public ResponseEntity<PurchaseRecordDto> createPurchase(@Valid @RequestBody PurchaseRecordCreateDto dto) {
        PurchaseRecordDto created = purchaseRecordFacade.createPurchase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRecordDto> getPurchase(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseRecordFacade.getPurchase(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseRecordDto>> getAllPurchases() {
        return ResponseEntity.ok(purchaseRecordFacade.getAllPurchases());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PurchaseRecordDto>> getByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(purchaseRecordFacade.getByCustomerId(customerId));
    }

    @GetMapping("/wine/{wineId}")
    public ResponseEntity<List<PurchaseRecordDto>> getByWineId(@PathVariable UUID wineId) {
        return ResponseEntity.ok(purchaseRecordFacade.getByWineId(wineId));
    }

    @GetMapping("/quantity")
    public ResponseEntity<List<PurchaseRecordDto>> getByQuantity(
        @RequestParam(required = false) Integer min,
        @RequestParam(required = false) Integer max) {

        return ResponseEntity.ok(purchaseRecordFacade.getByQuantity(min, max));
    }

    @GetMapping("/customer/{customerId}/total-spent")
    public ResponseEntity<Double> getTotalSpent(@PathVariable UUID customerId) {
        return ResponseEntity.ok(purchaseRecordFacade.getTotalSpentByCustomer(customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable UUID id) {
        purchaseRecordFacade.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
