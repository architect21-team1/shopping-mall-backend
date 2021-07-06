package dev.lucasdeabreu.saga.refund;

import dev.lucasdeabreu.saga.refund.event.RefundCreateEvent;
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
public class RefundEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueRefundCreateName;

    public RefundEventListener(RabbitTemplate rabbitTemplate,
                               Converter converter,
                               @Value("${queue.refund-create}") String queueRefundCreateName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueRefundCreateName = queueRefundCreateName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRefundCreateEvent(RefundCreateEvent event) {
        log.debug("Sending refund created event to {}, event: {}", queueRefundCreateName, event);
        rabbitTemplate.convertAndSend(queueRefundCreateName, converter.toJSON(event));
    }

}
