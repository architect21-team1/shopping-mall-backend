package dev.lucasdeabreu.saga.refund.handler;

import dev.lucasdeabreu.saga.refund.RefundService;
import dev.lucasdeabreu.saga.refund.event.OrderCancelEvent;
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
public class OrderCancelHandler {

    private final Converter converter;
    private final RefundService refundService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.order-cancel}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a order cancel event {}", payload);
        OrderCancelEvent event = converter.toObject(payload, OrderCancelEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        refundService.completeRefund(event.getRefund());
    }
}
