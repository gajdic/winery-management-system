package cz.muni.fi.pa165.productionservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.messaging.Queues;
import cz.muni.fi.pa165.commonservice.messaging.data.OutboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.entity.OutboundMessageLog;
import cz.muni.fi.pa165.commonservice.messaging.messages.BatchCompletedMessage;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductionBatchCompletedProducer {
    private final JmsTemplate jmsTemplate;
    private final OutboundMessageLogRepository outboundMessageLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    public ProductionBatchCompletedProducer(
        JmsTemplate jmsTemplate,
        OutboundMessageLogRepository outboundMessageLogRepository
    ) {
        this.jmsTemplate = jmsTemplate;
        this.outboundMessageLogRepository = outboundMessageLogRepository;
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

        jmsTemplate.convertAndSend(Queues.BATCH_COMPLETED, message);
    }
}
