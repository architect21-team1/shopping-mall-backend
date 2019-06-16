package dev.lucasdeabreu.orderservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Component
public class OrderCreatedEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueOrderCreateName;

    public OrderCreatedEventListener(RabbitTemplate rabbitTemplate,
                                     Converter converter,
                                     @Value("${queue.order-create}") String queueOrderCreateName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueOrderCreateName = queueOrderCreateName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateEvent(OrderCreatedEvent event) {
        log.debug("Sending order created event to {}, event: {}", queueOrderCreateName, event);
        rabbitTemplate.convertAndSend(queueOrderCreateName, converter.toJSON(event));
    }

}
