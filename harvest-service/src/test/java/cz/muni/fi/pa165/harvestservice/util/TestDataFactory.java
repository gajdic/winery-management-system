package cz.muni.fi.pa165.harvestservice.util;

import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.GrapeTypeViewDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.HarvestRecordViewDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.data.model.GrapeType;
import cz.muni.fi.pa165.harvestservice.data.model.HarvestRecord;
import cz.muni.fi.pa165.harvestservice.data.model.Vineyard;

import java.time.LocalDate;
import java.util.UUID;

public class TestDataFactory {

    public static final UUID VINEYARD_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID GRAPE_TYPE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID HARVEST_RECORD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    public static Vineyard vineyardEntity() {
        Vineyard vineyard = new Vineyard();
        vineyard.setId(VINEYARD_ID);
        vineyard.setName("Moravské vinice");
        vineyard.setLocation("Brno, Morava");
        vineyard.setAreaSize(5.5);
        vineyard.setDescription("Nejstarší vinice na Moravě");
        return vineyard;
    }

    public static GrapeType grapeTypeEntity() {
        GrapeType grapeType = new GrapeType();
        grapeType.setId(GRAPE_TYPE_ID);
        grapeType.setName("Welschriesling");
        grapeType.setDescription("Oblíbená odrůda na Moravě");
        return grapeType;
    }

    public static HarvestRecord harvestRecordEntity() {
        HarvestRecord record = new HarvestRecord();
        record.setId(HARVEST_RECORD_ID);
        record.setVineyard(vineyardEntity());
        record.setGrapeType(grapeTypeEntity());
        record.setHarvestDate(LocalDate.of(2024, 9, 15));
        record.setQuantity(1500.0);
        record.setQuality(8);
        return record;
    }

    public static VineyardViewDto vineyardViewDto() {
        return new VineyardViewDto(
                VINEYARD_ID,
                "Moravské vinice",
                "Brno, Morava",
                5.5,
                "Nejstarší vinice na Moravě"
        );
    }

    public static VineyardCreateDto vineyardCreateDto() {
        return new VineyardCreateDto(
                "Moravské vinice",
                "Brno, Morava",
                5.5,
                "Nejstarší vinice na Moravě"
        );
    }

    public static GrapeTypeViewDto grapeTypeViewDto() {
        return new GrapeTypeViewDto(
                GRAPE_TYPE_ID,
                "Welschriesling",
                "Oblíbená odrůda na Moravě"
        );
    }

    public static GrapeTypeCreateDto grapeTypeCreateDto() {
        return new GrapeTypeCreateDto("Welschriesling", "Oblíbená odrůda na Moravě");
    }

    public static HarvestRecordViewDto harvestRecordViewDto() {
        return new HarvestRecordViewDto(
                HARVEST_RECORD_ID,
                GRAPE_TYPE_ID,
                "Welschriesling",
                VINEYARD_ID,
                "Moravské vinice",
                LocalDate.of(2024, 9, 15),
                1500.0,
                8
        );
    }

    public static HarvestRecordCreateDto harvestRecordCreateDto() {
        return new HarvestRecordCreateDto(
                GRAPE_TYPE_ID,
                VINEYARD_ID,
                LocalDate.of(2024, 9, 15),
                1500.0,
                8
        );
    }
}
