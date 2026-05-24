package cz.muni.fi.pa165.productionservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.messaging.Queues;
import cz.muni.fi.pa165.commonservice.messaging.data.InboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.OutboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.entity.InboundMessageLog;
import cz.muni.fi.pa165.commonservice.messaging.data.entity.OutboundMessageLog;
import cz.muni.fi.pa165.commonservice.messaging.messages.BatchCompletedMessage;
import cz.muni.fi.pa165.commonservice.messaging.messages.StockUpdatedMessage;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class ProductionBatchCompletedProducer {
    private final JmsTemplate jmsTemplate;
    private final OutboundMessageLogRepository outboundMessageLogRepository;
    private final InboundMessageLogRepository inboundMessageLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    public ProductionBatchCompletedProducer(
        JmsTemplate jmsTemplate,
        OutboundMessageLogRepository outboundMessageLogRepository,
        InboundMessageLogRepository inboundMessageLogrepository
    ) {
        this.jmsTemplate = jmsTemplate;
        this.outboundMessageLogRepository = outboundMessageLogRepository;
        this.inboundMessageLogRepository = inboundMessageLogrepository;
    }

    public void sendBatchCompleted(ProductionBatch batch) {
        int bottleCount = batch.getActualBottleCount() > 0 ? batch.getActualBottleCount() : batch.getPlannedBottleCount();
        BatchCompletedMessage message = new BatchCompletedMessage(
            batch.getId(),
            batch.getRecipeId(),
            bottleCount,
            batch.getPlannedLiters(),
            LocalDateTime.now()
        );

        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize BatchCompletedMessage", e);
        }

        OutboundMessageLog messageLog = new OutboundMessageLog();
        messageLog.setQueueName(Queues.BATCH_COMPLETED);
        messageLog.setPayload(payload);
        messageLog.setBatchId(batch.getId());
        messageLog.setSentAt(LocalDateTime.now());
        outboundMessageLogRepository.save(messageLog);

        String correlationId = batch.getId().toString();
        log.info("Sending BatchCompletedMessage to {}, correlationId={}, bottleCount={}",
            Queues.BATCH_COMPLETED, correlationId, bottleCount);
        jmsTemplate.convertAndSend(Queues.BATCH_COMPLETED, message, jmsMessage -> {
            jmsMessage.setJMSCorrelationID(correlationId);
            return jmsMessage;
        });

        log.info("Waiting for StockUpdatedMessage ACK on {}, correlationId={}", Queues.STOCK_UPDATED, correlationId);
        Object ack = jmsTemplate.receiveSelectedAndConvert(
            Queues.STOCK_UPDATED,
            "JMSCorrelationID = '" + correlationId + "'"
        );
        validateAck(batch.getId(), batch.getRecipeId(), ack);
        log.info("Received valid ACK for batch {}, saving to inbound_message_log", batch.getId());

        String ackPayload;
        try {
            ackPayload = objectMapper.writeValueAsString(ack);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize ACK message", e);
        }

        InboundMessageLog inboundLog = new InboundMessageLog();
        inboundLog.setQueueName(Queues.STOCK_UPDATED);
        inboundLog.setPayload(ackPayload);
        inboundLog.setBatchId(batch.getId());
        inboundLog.setReceivedAt(LocalDateTime.now());
        inboundMessageLogRepository.save(inboundLog);
    }

    private void validateAck(UUID batchId, UUID wineId, Object ack) {
        if (ack == null) {
            throw new IllegalStateException("Did not receive stock update ACK for batch " + batchId);
        }
        if (!(ack instanceof StockUpdatedMessage stockUpdatedMessage)) {
            throw new IllegalStateException("Received unexpected ACK payload type for batch " + batchId);
        }
        if (!batchId.equals(stockUpdatedMessage.getBatchId()) || !wineId.equals(stockUpdatedMessage.getWineId())) {
            throw new IllegalStateException("Received mismatched stock update ACK for batch " + batchId);
        }
    }
}
