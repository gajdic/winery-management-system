package cz.muni.fi.pa165.customerservice.facade;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import cz.muni.fi.pa165.customerservice.mappers.PurchaseRecordMapper;
import cz.muni.fi.pa165.customerservice.service.PurchaseRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseRecordFacade {

    private final PurchaseRecordService purchaseRecordService;
    private final PurchaseRecordMapper purchaseRecordMapper;

    public PurchaseRecordFacade(PurchaseRecordService purchaseRecordService,
                          PurchaseRecordMapper purchaseMapper) {
        this.purchaseRecordService = purchaseRecordService;
        this.purchaseRecordMapper = purchaseMapper;
    }

    public PurchaseRecordDto createPurchase(PurchaseRecordCreateDto dto) {
        PurchaseRecord purchase = purchaseRecordMapper.mapToEntity(dto);
        PurchaseRecord saved = purchaseRecordService.save(purchase);
        return purchaseRecordMapper.mapToDto(saved);
    }

    public PurchaseRecordDto getPurchase(UUID id) {
        PurchaseRecord purchase = purchaseRecordService.findById(id);
        return purchaseRecordMapper.mapToDto(purchase);
    }

    public List<PurchaseRecordDto> getAllPurchases() {
        return purchaseRecordService.findAll()
            .stream()
            .map(purchaseRecordMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public List<PurchaseRecordDto> getByCustomerId(UUID customerId) {
        return purchaseRecordService.findByCustomerId(customerId)
            .stream()
            .map(purchaseRecordMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public List<PurchaseRecordDto> getByWineId(UUID wineId) {
        return purchaseRecordService.findByWineId(wineId)
            .stream()
            .map(purchaseRecordMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public List<PurchaseRecordDto> getByQuantity(Integer min, Integer max) {
        return purchaseRecordService.findByQuantity(min, max)
            .stream()
            .map(purchaseRecordMapper::mapToDto)
            .collect(Collectors.toList());
    }

    public double getTotalSpentByCustomer(UUID customerId) {
        return purchaseRecordService.getTotalSpentByCustomer(customerId);
    }

    public void deletePurchase(UUID id) {
        purchaseRecordService.deleteById(id);
    }
}
