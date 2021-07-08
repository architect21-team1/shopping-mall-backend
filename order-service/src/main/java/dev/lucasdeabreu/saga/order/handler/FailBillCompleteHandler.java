package dev.lucasdeabreu.saga.order.handler;

import dev.lucasdeabreu.saga.order.OrderService;
import dev.lucasdeabreu.saga.order.event.FailBillCompleteEvent;
import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class FailBillCompleteHandler {

    private final Converter converter;
    private final OrderService orderService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.fail-bill-complete}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a fail bill cancel event {}", payload);
        FailBillCompleteEvent event = converter.toObject(payload, FailBillCompleteEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        orderService.cancel(event.getOrder());
    }
}
