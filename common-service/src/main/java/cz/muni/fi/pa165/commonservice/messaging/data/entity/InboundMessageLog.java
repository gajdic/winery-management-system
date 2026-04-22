package cz.muni.fi.pa165.commonservice.messaging.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inbound_message_log")
@Getter
@Setter
public class InboundMessageLog extends cz.muni.fi.pa165.commonservice.entity.EntityBase {

    @Column(nullable = false)
    private String queueName;

    @Column(nullable = false, length = 4000)
    private String payload;

    @Column(nullable = false)
    private UUID batchId;

    @Column(nullable = false)
    private LocalDateTime receivedAt;
}
