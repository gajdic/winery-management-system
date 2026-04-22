package cz.muni.fi.pa165.productionservice.messaging.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbound_message_log")
@Getter
@Setter
public class OutboundMessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String queueName;

    @Column(nullable = false, length = 4000)
    private String payload;

    @Column(nullable = false)
    private UUID batchId;

    @Column(nullable = false)
    private LocalDateTime sentAt;
}
