package cz.muni.fi.pa165.harvestservice.util;

import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeBasicDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.GrapeTypeViewDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.HarvestRecordViewDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardBasicDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardCreateDto;
import cz.muni.fi.pa165.commonservice.api.harvest.VineyardViewDto;
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
        vineyard.setName("Moravsk\u00e9 vinice");
        vineyard.setLocation("Brno, Morava");
        vineyard.setAreaSize(5.5);
        vineyard.setDescription("Nejstar\u0161\u00ed vinice na Morav\u011b");
        return vineyard;
    }

    public static Vineyard vineyardEntity(String name, String location) {
        Vineyard vineyard = new Vineyard();
        vineyard.setName(name);
        vineyard.setLocation(location);
        vineyard.setAreaSize(10.0);
        vineyard.setDescription(name + " description");
        return vineyard;
    }

    public static GrapeType grapeTypeEntity() {
        GrapeType grapeType = new GrapeType();
        grapeType.setId(GRAPE_TYPE_ID);
        grapeType.setName("Welschriesling");
        grapeType.setDescription("Obl\u00edben\u00e1 odr\u016fda na Morav\u011b");
        return grapeType;
    }

    public static GrapeType grapeTypeEntity(String name) {
        GrapeType grapeType = new GrapeType();
        grapeType.setName(name);
        grapeType.setDescription(name + " description");
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

    public static HarvestRecord harvestRecordEntity(GrapeType grapeType, Vineyard vineyard, LocalDate harvestDate) {
        HarvestRecord record = new HarvestRecord();
        record.setGrapeType(grapeType);
        record.setVineyard(vineyard);
        record.setHarvestDate(harvestDate);
        record.setQuantity(1200.0);
        record.setQuality(8);
        return record;
    }

    public static VineyardViewDto vineyardViewDto() {
        return new VineyardViewDto(
            VINEYARD_ID,
            "Moravsk\u00e9 vinice",
            "Brno, Morava",
            5.5,
            "Nejstar\u0161\u00ed vinice na Morav\u011b"
        );
    }

    public static VineyardCreateDto vineyardCreateDto() {
        return new VineyardCreateDto(
            "Moravsk\u00e9 vinice",
            "Brno, Morava",
            5.5,
            "Nejstar\u0161\u00ed vinice na Morav\u011b"
        );
    }

    public static GrapeTypeViewDto grapeTypeViewDto() {
        return new GrapeTypeViewDto(
            GRAPE_TYPE_ID,
            "Welschriesling",
            "Obl\u00edben\u00e1 odr\u016fda na Morav\u011b"
        );
    }

    public static GrapeTypeCreateDto grapeTypeCreateDto() {
        return new GrapeTypeCreateDto("Welschriesling", "Obl\u00edben\u00e1 odr\u016fda na Morav\u011b");
    }

    public static HarvestRecordViewDto harvestRecordViewDto() {
        return new HarvestRecordViewDto(
            HARVEST_RECORD_ID,
            new GrapeTypeBasicDto(GRAPE_TYPE_ID, "Welschriesling"),
            new VineyardBasicDto(VINEYARD_ID, "Moravsk\u00e9 vinice"),
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

