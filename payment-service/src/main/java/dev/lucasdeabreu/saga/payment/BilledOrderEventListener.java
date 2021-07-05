package dev.lucasdeabreu.saga.payment;

import dev.lucasdeabreu.saga.payment.event.BilledOrderEvent;
import dev.lucasdeabreu.saga.payment.event.FailPreparedProductEvent;
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
public class BilledOrderEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueBilledOrderName;
    private final String queueFailPreparedProductName;

    public BilledOrderEventListener(RabbitTemplate rabbitTemplate,
                                    Converter converter,
                                    @Value("${queue.billed-order}") String queueBilledOrderName,
                                    @Value("${queue.fail-prepared-product}") String queueFailPreparedProductName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueBilledOrderName = queueBilledOrderName;
        this.queueFailPreparedProductName = queueFailPreparedProductName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBilledOrderEvent(BilledOrderEvent event) {
        log.debug("Sending billed order event to {}, event: {}", queueBilledOrderName, event);
        rabbitTemplate.convertAndSend(queueBilledOrderName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onFailPreparedProductEvent(FailPreparedProductEvent event) {
        log.debug("Sending fail prepared product event to {}, event: {}", queueFailPreparedProductName, event);
        rabbitTemplate.convertAndSend(queueFailPreparedProductName, converter.toJSON(event));
    }

}
