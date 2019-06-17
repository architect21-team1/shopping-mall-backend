package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class OrderCanceledHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = {"${queue.order-canceled}"})
    public void onRefundPayment(@Payload String payload) {
        log.debug("Handling a refund order event {}", payload);
        OrderCanceledEvent event = converter.toObject(payload, OrderCanceledEvent.class);
        orderService.cancelOrder(event.getOrder().getTransactionId(), event.getReason());
    }
}
