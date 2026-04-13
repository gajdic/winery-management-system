package cz.muni.fi.pa165.inventoryservice.util;

import cz.muni.fi.pa165.inventoryservice.api.ReservationBasicViewDto;
import cz.muni.fi.pa165.inventoryservice.api.ReservationDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.api.StockDetailedViewDto;
import cz.muni.fi.pa165.inventoryservice.api.StockQuantityChangeDto;
import cz.muni.fi.pa165.inventoryservice.data.enums.ReservationStatus;
import cz.muni.fi.pa165.inventoryservice.data.models.Reservation;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class TestDataFactory {

    public static final UUID STOCK_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID RESERVATION_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID WINE_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID CUSTOMER_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    private TestDataFactory() {
    }

    public static Stock stockEntity() {
        return stockEntity(120, 20);
    }

    public static Stock stockEntity(int quantity, int reservedQuantity) {
        Stock stock = new Stock();
        stock.setId(STOCK_ID);
        stock.setWineId(WINE_ID);
        stock.setQuantity(quantity);
        stock.setReservedQuantity(reservedQuantity);
        return stock;
    }

    public static Optional<Stock> stockOptionalEntity(int quantity, int reservedQuantity){
        return Optional.of(stockEntity(quantity, reservedQuantity));
    }

    public static Reservation reservationEntity() {
        return reservationEntity(ReservationStatus.PENDING, 15);
    }

    public static Reservation reservationEntity(ReservationStatus status, int quantity) {
        Reservation reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
        reservation.setCustomerId(CUSTOMER_ID);
        reservation.setWineId(WINE_ID);
        reservation.setQuantity(quantity);
        reservation.setStatus(status);
        reservation.setCreatedAt(LocalDate.of(2025, 3, 1));
        return reservation;
    }
    public static StockDetailedViewDto stockDetailedViewDto() {
        StockDetailedViewDto dto = new StockDetailedViewDto();
        dto.setId(STOCK_ID);
        dto.setWineId(WINE_ID);
        dto.setQuantity(120);
        dto.setReservedQuantity(20);
        return dto;
    }

    public static StockDetailedViewDto stockDetailedViewDto(int quantity, int reservedQuantity) {
        StockDetailedViewDto dto = new StockDetailedViewDto();
        dto.setId(STOCK_ID);
        dto.setWineId(WINE_ID);
        dto.setQuantity(quantity);
        dto.setReservedQuantity(reservedQuantity);
        return dto;
    }

    public static ReservationBasicViewDto reservationBasicViewDto() {
        ReservationBasicViewDto dto = new ReservationBasicViewDto();
        dto.setId(RESERVATION_ID);
        dto.setStatus(ReservationStatus.PENDING);
        dto.setCreatedAt(LocalDate.of(2025, 3, 1));
        return dto;
    }

    public static ReservationDetailedViewDto reservationDetailedViewDto() {
        return reservationDetailedViewDto(ReservationStatus.PENDING, 15);
    }

    public static ReservationDetailedViewDto reservationDetailedViewDto(ReservationStatus status, int quantity) {
        ReservationDetailedViewDto dto = new ReservationDetailedViewDto();
        dto.setId(RESERVATION_ID);
        dto.setCustomerId(CUSTOMER_ID);
        dto.setWineId(WINE_ID);
        dto.setQuantity(quantity);
        dto.setStatus(status);
        dto.setCreatedAt(LocalDate.of(2025, 3, 1));
        return dto;
    }

    public static StockQuantityChangeDto stockQuantityChangeDto() {
        return stockQuantityChangeDto(10);
    }

    public static StockQuantityChangeDto stockQuantityChangeDto(int quantity) {
        StockQuantityChangeDto dto = new StockQuantityChangeDto();
        dto.setQuantity(quantity);
        return dto;
    }

    public static List<StockDetailedViewDto> stockDetailedViewDtos() {
        return List.of(stockDetailedViewDto());
    }

    public static List<ReservationBasicViewDto> reservationBasicViewDtos() {
        return List.of(reservationBasicViewDto());
    }
}
