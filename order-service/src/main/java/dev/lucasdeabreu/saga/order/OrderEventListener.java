package dev.lucasdeabreu.saga.order;

import dev.lucasdeabreu.saga.order.event.OrderCancelEvent;
import dev.lucasdeabreu.saga.order.event.OrderCreatedEvent;
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
public class OrderEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueOrderCreatedName;
    private final String queueOrderCancelName;

    public OrderEventListener(RabbitTemplate rabbitTemplate,
                              Converter converter,
                              @Value("${queue.order-created}") String queueOrderCreateName,
                              @Value("${queue.order-cancel}") String queueOrderCancelName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueOrderCreatedName = queueOrderCreateName;
        this.queueOrderCancelName = queueOrderCancelName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderCreatedEvent event) {
        log.debug("Sending order created event to {}, event: {}", queueOrderCreatedName, event);
        rabbitTemplate.convertAndSend(queueOrderCreatedName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancelEvent(OrderCancelEvent event) {
        log.debug("Sending order created event to {}, event: {}", queueOrderCancelName, event);
        rabbitTemplate.convertAndSend(queueOrderCancelName, converter.toJSON(event));
    }

}
