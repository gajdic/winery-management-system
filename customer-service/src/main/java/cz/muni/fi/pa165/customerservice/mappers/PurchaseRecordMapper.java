package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRecordMapper {

    @Mapping(target = "customerId", source = "customer.id")
    PurchaseRecordDto mapToDto(PurchaseRecord purchaseRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseDate", ignore = true)
    @Mapping(target = "customer.id", source = "customerId")
    PurchaseRecord mapToEntity(PurchaseRecordCreateDto dto);
}
