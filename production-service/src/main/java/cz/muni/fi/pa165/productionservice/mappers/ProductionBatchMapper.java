package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.springframework.stereotype.Service;

@Service
public class ProductionBatchMapper {
    public ProductionBatchDto toDto(ProductionBatch entity) {
        ProductionBatchDto dto = new ProductionBatchDto();
        dto.setId(entity.getId());
        dto.setRecipeId(entity.getRecipeId());
        dto.setPlannedLiters(entity.getPlannedLiters());
        dto.setPlannedBottleCount(entity.getPlannedBottleCount());
        dto.setActualBottleCount(entity.getActualBottleCount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
