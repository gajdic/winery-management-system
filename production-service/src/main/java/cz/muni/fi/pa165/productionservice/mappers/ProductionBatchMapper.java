package cz.muni.fi.pa165.productionservice.mappers;

import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductionBatchMapper {
    ProductionBatchDto mapToDto(ProductionBatch entity);
}
