package dev.lucasdeabreu.saga.stock;

import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.stock.event.OrderCompleteEvent;
import dev.lucasdeabreu.saga.stock.event.RefundCompleteEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Component
public class StockEventListener {

    private static final String ROUTING_KEY = "";

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueOrderDoneName;
    private final String queueRefundCompleteName;
    private final String queueFailRefundCompleteName;
    private final String topicOrderCanceledName;

    public StockEventListener(RabbitTemplate rabbitTemplate,
                              Converter converter,
                              @Value("${queue.order-done}") String queueOrderDoneName,
                              @Value("${queue.refund-complete}") String queueRefundCompleteName,
                              @Value("${queue.fail-refund-complete}") String queueFailRefundCompleteName,
                              @Value("${topic.order-canceled}") String topicOrderCanceledName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueOrderDoneName = queueOrderDoneName;
        this.queueRefundCompleteName = queueRefundCompleteName;
        this.queueFailRefundCompleteName = queueFailRefundCompleteName;
        this.topicOrderCanceledName = topicOrderCanceledName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderDoneEvent(OrderCompleteEvent event) {
        log.debug("Sending order done event to {}, event: {}", queueOrderDoneName, event);
        rabbitTemplate.convertAndSend(queueOrderDoneName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRefundCompleteEvent(RefundCompleteEvent event) {
        log.debug("Sending refund complete event to {}, event: {}", queueRefundCompleteName, event);
        rabbitTemplate.convertAndSend(queueRefundCompleteName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onFailRefundCompleteEvent(RefundCompleteEvent event) {
        log.debug("Sending refund complete event to {}, event: {}", queueFailRefundCompleteName, event);
        rabbitTemplate.convertAndSend(queueFailRefundCompleteName, converter.toJSON(event));
    }
}
