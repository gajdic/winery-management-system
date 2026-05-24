package cz.muni.fi.pa165.inventoryservice.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.messaging.Queues;
import cz.muni.fi.pa165.commonservice.messaging.data.InboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.entity.InboundMessageLog;
import cz.muni.fi.pa165.commonservice.messaging.messages.BatchCompletedMessage;
import cz.muni.fi.pa165.commonservice.messaging.messages.StockUpdatedMessage;
import cz.muni.fi.pa165.inventoryservice.data.models.Stock;
import cz.muni.fi.pa165.inventoryservice.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class BatchCompletedConsumer {

    private final StockService stockService;
    private final InboundMessageLogRepository messageLogRepository;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public BatchCompletedConsumer(StockService stockService,
                                  InboundMessageLogRepository messageLogRepository,
                                  JmsTemplate jmsTemplate) {
        this.stockService = stockService;
        this.messageLogRepository = messageLogRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    @JmsListener(destination = Queues.BATCH_COMPLETED)
    public void onBatchCompleted(BatchCompletedMessage message) throws Exception {
        log.info("Received BatchCompletedMessage: {}", message);

        InboundMessageLog log = new InboundMessageLog();
        log.setQueueName(Queues.BATCH_COMPLETED);
        log.setPayload(objectMapper.writeValueAsString(message));
        log.setBatchId(message.getBatchId());
        log.setReceivedAt(LocalDateTime.now());
        messageLogRepository.save(log);

        Stock existing = stockService.findByWine(message.getWineId());
        Stock stock;
        if (existing == null) {
            stock = new Stock();
            stock.setQuantity(message.getBottleCount());
            stock.setWineId(message.getWineId());
            stock = stockService.create(stock);
        } else {
            stock = stockService.addToStock(existing.getId(), message.getBottleCount());
        }

        StockUpdatedMessage ack = new StockUpdatedMessage(
            message.getBatchId(),
            message.getWineId(),
            stock.getQuantity(),
            LocalDateTime.now()
        );

        String correlationId = message.getBatchId().toString();
        jmsTemplate.convertAndSend(Queues.STOCK_UPDATED, ack, jmsMessage -> {
            jmsMessage.setJMSCorrelationID(correlationId);
            return jmsMessage;
        });
    }
}
