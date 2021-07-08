package dev.lucasdeabreu.saga.product;

import dev.lucasdeabreu.saga.product.event.OrderCompleteEvent;
import dev.lucasdeabreu.saga.product.event.RefundCompleteEvent;
import dev.lucasdeabreu.saga.shared.Converter;
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
    private final String queueOrderCompleteName;
    private final String queueRefundCompleteName;
    private final String queueFailRefundCompleteName;

    public StockEventListener(RabbitTemplate rabbitTemplate,
                              Converter converter,
                              @Value("${queue.order-complete}") String queueOrderCompleteName,
                              @Value("${queue.refund-complete}") String queueRefundCompleteName,
                              @Value("${queue.fail-refund-complete}") String queueFailRefundCompleteName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueOrderCompleteName = queueOrderCompleteName;
        this.queueRefundCompleteName = queueRefundCompleteName;
        this.queueFailRefundCompleteName = queueFailRefundCompleteName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCompleteEvent(OrderCompleteEvent event) {
        log.debug("Sending order complete event to {}, event: {}", queueOrderCompleteName, event);
        rabbitTemplate.convertAndSend(queueOrderCompleteName, converter.toJSON(event));
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
