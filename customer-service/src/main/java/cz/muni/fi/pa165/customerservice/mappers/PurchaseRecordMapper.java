package cz.muni.fi.pa165.customerservice.mappers;

import cz.muni.fi.pa165.customerservice.api.PurchaseRecordCreateDto;
import cz.muni.fi.pa165.customerservice.api.PurchaseRecordDto;
import cz.muni.fi.pa165.customerservice.data.model.PurchaseRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseRecordMapper {

    PurchaseRecordDto mapToDto(PurchaseRecord purchaseRecord);

    PurchaseRecord mapToEntity(PurchaseRecordCreateDto dto);
}
