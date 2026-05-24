package cz.muni.fi.pa165.productionservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.messaging.Queues;
import cz.muni.fi.pa165.commonservice.messaging.data.InboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.OutboundMessageLogRepository;
import cz.muni.fi.pa165.commonservice.messaging.data.entity.OutboundMessageLog;
import cz.muni.fi.pa165.commonservice.messaging.messages.BatchCompletedMessage;
import cz.muni.fi.pa165.commonservice.messaging.messages.StockUpdatedMessage;
import cz.muni.fi.pa165.productionservice.data.model.ProductionBatch;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import jakarta.jms.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionBatchCompletedProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private InboundMessageLogRepository inboundMessageLogRepository;

    @Mock
    private OutboundMessageLogRepository outboundMessageLogRepository;

    private ProductionBatchCompletedProducer producer;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Captor
    private ArgumentCaptor<BatchCompletedMessage> messageCaptor;

    @Captor
    private ArgumentCaptor<MessagePostProcessor> postProcessorCaptor;

    @Captor
    private ArgumentCaptor<OutboundMessageLog> logCaptor;

    @BeforeEach
    void setUp() {
        producer = new ProductionBatchCompletedProducer(
            jmsTemplate,
            outboundMessageLogRepository,
            inboundMessageLogRepository
        );
    }

    @Test
    void sendBatchCompleted_actualBottleCountIsZero_usesPlannedBottleCount() throws Exception {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        batch.setActualBottleCount(0);
        batch.setPlannedBottleCount(120);

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jmsTemplate.receiveSelectedAndConvert(
            eq(Queues.STOCK_UPDATED),
            eq("JMSCorrelationID = '" + batch.getId() + "'")
        )).thenReturn(stockUpdatedAck(batch));

        producer.sendBatchCompleted(batch);

        verify(jmsTemplate).convertAndSend(
            eq(Queues.BATCH_COMPLETED),
            messageCaptor.capture(),
            postProcessorCaptor.capture()
        );
        BatchCompletedMessage sentMessage = messageCaptor.getValue();

        assertThat(sentMessage.getBatchId()).isEqualTo(batch.getId());
        assertThat(sentMessage.getWineId()).isEqualTo(batch.getRecipeId());
        assertThat(sentMessage.getBottleCount()).isEqualTo(120);
        assertThat(sentMessage.getLiters()).isEqualTo(batch.getPlannedLiters());
        assertThat(sentMessage.getCompletedAt()).isNotNull();

        verify(outboundMessageLogRepository).save(logCaptor.capture());
        OutboundMessageLog savedLog = logCaptor.getValue();
        assertThat(savedLog.getQueueName()).isEqualTo(Queues.BATCH_COMPLETED);
        assertThat(savedLog.getBatchId()).isEqualTo(batch.getId());
        assertThat(savedLog.getSentAt()).isNotNull();

        BatchCompletedMessage payloadMessage =
            objectMapper.readValue(savedLog.getPayload(), BatchCompletedMessage.class);
        assertThat(payloadMessage.getWineId()).isEqualTo(batch.getRecipeId());
        assertThat(payloadMessage.getBottleCount()).isEqualTo(120);

        Message jmsMessage = Mockito.mock(Message.class);
        postProcessorCaptor.getValue().postProcessMessage(jmsMessage);
        verify(jmsMessage).setJMSCorrelationID(batch.getId().toString());
        verify(jmsTemplate).receiveSelectedAndConvert(
            Queues.STOCK_UPDATED,
            "JMSCorrelationID = '" + batch.getId() + "'"
        );
    }

    @Test
    void sendBatchCompleted_actualBottleCountGreaterThanZero_usesActualBottleCount() {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        batch.setActualBottleCount(77);
        batch.setPlannedBottleCount(120);

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jmsTemplate.receiveSelectedAndConvert(any(String.class), any(String.class))).thenReturn(stockUpdatedAck(batch));

        producer.sendBatchCompleted(batch);

        verify(jmsTemplate).convertAndSend(eq(Queues.BATCH_COMPLETED), messageCaptor.capture(), any());
        BatchCompletedMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getBottleCount()).isEqualTo(77);
    }

    @Test
    void sendBatchCompleted_ackIsMissing_throwsIllegalStateException() {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jmsTemplate.receiveSelectedAndConvert(any(String.class), any(String.class))).thenReturn(null);

        assertThatThrownBy(() -> producer.sendBatchCompleted(batch))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Did not receive stock update ACK");
    }

    @Test
    void sendBatchCompleted_ackHasMismatchedBatch_throwsIllegalStateException() {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        StockUpdatedMessage ack = stockUpdatedAck(batch);
        ack.setBatchId(UUID.randomUUID());

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jmsTemplate.receiveSelectedAndConvert(any(String.class), any(String.class))).thenReturn(ack);

        assertThatThrownBy(() -> producer.sendBatchCompleted(batch))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Received mismatched stock update ACK");
    }

    @Test
    void sendBatchCompleted_ackHasMismatchedWine_throwsIllegalStateException() {
        ProductionBatch batch = TestDataFactory.productionBatchEntity();
        StockUpdatedMessage ack = stockUpdatedAck(batch);
        ack.setWineId(UUID.randomUUID());

        when(outboundMessageLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jmsTemplate.receiveSelectedAndConvert(any(String.class), any(String.class))).thenReturn(ack);

        assertThatThrownBy(() -> producer.sendBatchCompleted(batch))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Received mismatched stock update ACK");
    }

    private StockUpdatedMessage stockUpdatedAck(ProductionBatch batch) {
        return new StockUpdatedMessage(
            batch.getId(),
            batch.getRecipeId(),
            batch.getActualBottleCount(),
            LocalDateTime.now()
        );
    }
}
