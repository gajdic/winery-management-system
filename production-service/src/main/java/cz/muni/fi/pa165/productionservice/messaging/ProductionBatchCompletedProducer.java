package cz.muni.fi.pa165.productionservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.messaging.data.OutboundMessageLogRepository;
import cz.muni.fi.pa165.productionservice.messaging.data.entity.OutboundMessageLog;
import cz.muni.fi.pa165.productionservice.messaging.messages.ProductionBatchCompletedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductionBatchCompletedProducer {
    private static final String BATCH_COMPLETED_QUEUE = "batch-completed-queue";

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
        ProductionBatchCompletedMessage message = new ProductionBatchCompletedMessage(
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

        jmsTemplate.convertAndSend(BATCH_COMPLETED_QUEUE, payload);

        OutboundMessageLog messageLog = new OutboundMessageLog();
        messageLog.setQueueName(BATCH_COMPLETED_QUEUE);
        messageLog.setPayload(payload);
        messageLog.setBatchId(batch.getId());
        messageLog.setSentAt(LocalDateTime.now());
        outboundMessageLogRepository.save(messageLog);
    }
}
