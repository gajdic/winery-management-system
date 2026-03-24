package cz.muni.fi.pa165.harvestservice.service;

import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;
import cz.muni.fi.pa165.harvestservice.data.repository.GrapeTypeRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.HarvestRecordRepository;
import cz.muni.fi.pa165.harvestservice.data.repository.VineyardRepository;
import cz.muni.fi.pa165.harvestservice.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class HarvestRecordService {

    private final HarvestRecordRepository harvestRecordRepository;
    private final VineyardRepository vineyardRepository;
    private final GrapeTypeRepository grapeTypeRepository;

    public HarvestRecordService(HarvestRecordRepository harvestRecordRepository,
                                VineyardRepository vineyardRepository,
                                GrapeTypeRepository grapeTypeRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
        this.vineyardRepository = vineyardRepository;
        this.grapeTypeRepository = grapeTypeRepository;
    }

    public HarvestRecord create(HarvestRecord harvestRecord) {
        if (harvestRecord == null) {
            throw new IllegalArgumentException("HarvestRecord cannot be null.");
        }

        if (harvestRecord.getVineyard() == null || harvestRecord.getVineyard().getId() == null) {
            throw new IllegalArgumentException("Vineyard cannot be null.");
        }

        if (harvestRecord.getGrapeType() == null || harvestRecord.getGrapeType().getId() == null) {
            throw new IllegalArgumentException("Grape type cannot be null.");
        }

        Vineyard vineyard = vineyardRepository.findById(harvestRecord.getVineyard().getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Vineyard with id " + harvestRecord.getVineyard().getId() + " was not found."
            ));

        GrapeType grapeType = grapeTypeRepository.findById(harvestRecord.getGrapeType().getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "GrapeType with id " + harvestRecord.getGrapeType().getId() + " was not found."
            ));

        if (harvestRecord.getHarvestDate() == null) {
            throw new IllegalArgumentException("Harvest date cannot be null.");
        }

        if (harvestRecord.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        if (harvestRecord.getQuality() < 1 || harvestRecord.getQuality() > 10) {
            throw new IllegalArgumentException("Quality must be between 1 and 10.");
        }

        harvestRecord.setVineyard(vineyard);
        harvestRecord.setGrapeType(grapeType);

        return harvestRecordRepository.save(harvestRecord);
    }

    public HarvestRecord findById(UUID id) {
        return harvestRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Harvest record with id " + id + " was not found."));
    }

    public List<HarvestRecord> findByVineyardId(UUID id) {
        return harvestRecordRepository.findByVineyardId(id);
    }

    public List<HarvestRecord> findByGrapeTypeId(UUID id) {
        return harvestRecordRepository.findByGrapeTypeId(id);
    }

    public List<HarvestRecord> findByYear(int year) {
        return harvestRecordRepository.findByYear(year);
    }

    public List<HarvestRecord> findAll() {
        return harvestRecordRepository.findAll();
    }

    public HarvestRecord update(UUID id,
                                UUID vineyardId,
                                UUID grapeTypeId,
                                LocalDate harvestDate,
                                double quantity,
                                int quality) {

        HarvestRecord existing = harvestRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Harvest record with id " + id + " was not found."
            ));

        if (vineyardId == null) {
            throw new IllegalArgumentException("Vineyard id cannot be null.");
        }

        if (grapeTypeId == null) {
            throw new IllegalArgumentException("Grape type id cannot be null.");
        }

        Vineyard vineyard = vineyardRepository.findById(vineyardId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Vineyard with id " + vineyardId + " was not found."
            ));

        GrapeType grapeType = grapeTypeRepository.findById(grapeTypeId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grape type with id " + grapeTypeId + " was not found."
            ));

        if (harvestDate == null) {
            throw new IllegalArgumentException("Harvest date cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        if (quality < 0 || quality > 10) {
            throw new IllegalArgumentException("Quality must be between 0 and 10.");
        }

        existing.setVineyard(vineyard);
        existing.setGrapeType(grapeType);
        existing.setHarvestDate(harvestDate);
        existing.setQuantity(quantity);
        existing.setQuality(quality);

        return harvestRecordRepository.save(existing);
    }

    public void deleteById(UUID id) {
        harvestRecordRepository.deleteById(id);
    }
}
