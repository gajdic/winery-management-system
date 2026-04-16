package cz.muni.fi.pa165.productionservice.data.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Production batch lifecycle status.")
public enum BatchStatus {
    PLANNED,
    IN_PRODUCTION,
    READY,
    COMPLETED,
    CANCELLED
}
