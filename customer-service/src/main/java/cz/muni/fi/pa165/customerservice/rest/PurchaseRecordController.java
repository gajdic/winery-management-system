package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.facade.PurchaseRecordFacade;
import jakarta.validation.Valid;
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
    public PurchaseRecordDto createPurchase(@Valid @RequestBody PurchaseRecordCreateDto dto) {
        return purchaseRecordFacade.createPurchase(dto);
    }

    @GetMapping("/{id}")
    public PurchaseRecordDto getPurchase(@PathVariable UUID id) {
        return purchaseRecordFacade.getPurchase(id);
    }

    @GetMapping
    public List<PurchaseRecordDto> getAllPurchases() {
        return purchaseRecordFacade.getAllPurchases();
    }

    @GetMapping("/customer/{customerId}")
    public List<PurchaseRecordDto> getByCustomerId(@PathVariable UUID customerId) {
        return purchaseRecordFacade.getByCustomerId(customerId);
    }

    @GetMapping("/wine/{wineId}")
    public List<PurchaseRecordDto> getByWineId(@PathVariable UUID wineId) {
        return purchaseRecordFacade.getByWineId(wineId);
    }

    @GetMapping("/quantity")
    public List<PurchaseRecordDto> getByQuantity(
        @RequestParam(required = false) Integer min,
        @RequestParam(required = false) Integer max) {

        return purchaseRecordFacade.getByQuantity(min, max);
    }

    @GetMapping("/customer/{customerId}/total-spent")
    public double getTotalSpentByCustomer(@PathVariable UUID customerId) {
        return purchaseRecordFacade.getTotalSpentByCustomer(customerId);
    }

    @DeleteMapping("/{id}")
    public void deletePurchase(@PathVariable UUID id) {
        purchaseRecordFacade.deletePurchase(id);
    }
}
