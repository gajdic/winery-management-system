package cz.muni.fi.pa165.productionservice.data.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Production batch lifecycle status.")
public enum BatchStatus {
    @Schema(description = "Batch is planned and waiting for production start.")
    PLANNED,
    @Schema(description = "Batch is currently being produced.")
    IN_PRODUCTION,
    @Schema(description = "Batch is finished in production and ready for next steps.")
    READY,
    @Schema(description = "Batch lifecycle is fully completed.")
    COMPLETED,
    @Schema(description = "Batch was cancelled before completion.")
    CANCELLED
}
