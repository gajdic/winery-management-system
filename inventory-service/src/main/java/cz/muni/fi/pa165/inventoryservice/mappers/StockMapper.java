package cz.muni.fi.pa165.inventoryservice.mappers;

import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDetailedViewDto mapToDetailViewDto(Stock stock);

    List<StockDetailedViewDto> mapToList(List<Stock> stocks);

    default Page<StockDetailedViewDto> mapToPageDto(Page<Stock> stocks) {
        return new PageImpl<>(mapToList(stocks.getContent()), stocks.getPageable(), stocks.getTotalPages());
    }
}
