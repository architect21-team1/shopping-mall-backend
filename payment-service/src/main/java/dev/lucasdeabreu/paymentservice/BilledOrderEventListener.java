package dev.lucasdeabreu.paymentservice;

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

    private static final String ROUTING_KEY = "";

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String topicBilledOrderCreateName;

    public BilledOrderEventListener(RabbitTemplate rabbitTemplate,
                                    Converter converter,
                                    @Value("${topic.billed-order}") String topicBilledOrderCreateName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.topicBilledOrderCreateName = topicBilledOrderCreateName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBilledOrderEvent(BilledOrderEvent event) {
        log.debug("Sending billed order event to {}, event: {}", topicBilledOrderCreateName, event);
        rabbitTemplate.convertAndSend(topicBilledOrderCreateName, ROUTING_KEY, converter.toJSON(event));
    }

}
