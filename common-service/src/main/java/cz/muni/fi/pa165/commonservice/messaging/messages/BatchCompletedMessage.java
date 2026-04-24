package cz.muni.fi.pa165.commonservice.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchCompletedMessage {
    private UUID batchId;
    private UUID wineId;
    private int bottleCount;
    private double liters;
    private LocalDateTime completedAt;
}

