package cz.muni.fi.pa165.productionservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.messaging.data.OutboundMessageLogRepository;
import cz.muni.fi.pa165.productionservice.messaging.data.entity.OutboundMessageLog;
import cz.muni.fi.pa165.productionservice.messaging.messages.ProductionBatchCompletedMessage;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionBatchCompletedProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private OutboundMessageLogRepository outboundMessageLogRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private ProductionBatchCompletedProducer producer;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Captor
    private ArgumentCaptor<OutboundMessageLog> logCaptor;

    @Test
    void sendBatchCompleted_usesPlannedBottleCountWhenActualIsZero() throws Exception {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        batch.setActualBottleCount(0);
        batch.setPlannedBottleCount(120);

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        producer.sendBatchCompleted(batch);

        verify(jmsTemplate).convertAndSend(eq("batch-completed-queue"), messageCaptor.capture());
        ProductionBatchCompletedMessage sentMessage =
            objectMapper.readValue(messageCaptor.getValue(), ProductionBatchCompletedMessage.class);

        assertThat(sentMessage.getBatchId()).isEqualTo(batch.getId());
        assertThat(sentMessage.getWineId()).isEqualTo(batch.getRecipeId());
        assertThat(sentMessage.getBottleCount()).isEqualTo(120);
        assertThat(sentMessage.getLiters()).isEqualTo(batch.getPlannedLiters());
        assertThat(sentMessage.getCompletedAt()).isNotNull();

        verify(outboundMessageLogRepository).save(logCaptor.capture());
        OutboundMessageLog savedLog = logCaptor.getValue();
        assertThat(savedLog.getQueueName()).isEqualTo("batch-completed-queue");
        assertThat(savedLog.getBatchId()).isEqualTo(batch.getId());
        assertThat(savedLog.getSentAt()).isNotNull();

        ProductionBatchCompletedMessage payloadMessage =
            objectMapper.readValue(savedLog.getPayload(), ProductionBatchCompletedMessage.class);
        assertThat(payloadMessage.getWineId()).isEqualTo(batch.getRecipeId());
        assertThat(payloadMessage.getBottleCount()).isEqualTo(120);
    }

    @Test
    void sendBatchCompleted_usesActualBottleCountWhenPresent() throws Exception {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        batch.setActualBottleCount(77);
        batch.setPlannedBottleCount(120);

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        producer.sendBatchCompleted(batch);

        verify(jmsTemplate).convertAndSend(eq("batch-completed-queue"), messageCaptor.capture());
        ProductionBatchCompletedMessage sentMessage =
            objectMapper.readValue(messageCaptor.getValue(), ProductionBatchCompletedMessage.class);
        assertThat(sentMessage.getBottleCount()).isEqualTo(77);
    }
}
