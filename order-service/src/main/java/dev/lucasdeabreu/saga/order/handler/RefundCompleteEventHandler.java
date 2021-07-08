package dev.lucasdeabreu.saga.order.handler;

import dev.lucasdeabreu.saga.order.OrderService;
import dev.lucasdeabreu.saga.order.event.OrderCancelEvent;
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
public class RefundCompleteEventHandler {

    private final Converter converter;
    private final OrderService orderService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.refund-complete}"})
    public void onRefundComplete(@Payload String payload) {
        log.debug("Handling a refund complete event {}", payload);
        OrderCancelEvent event = converter.toObject(payload, OrderCancelEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        orderService.orderCancel(event.getRefund());
    }
}
