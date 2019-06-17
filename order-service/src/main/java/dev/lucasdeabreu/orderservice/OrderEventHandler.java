package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class OrderEventHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = {"${queue.order-done}"})
    public void handleOrderDoneEvent(@Payload String payload) {
        log.debug("Handling a order done event {}", payload);
        OrderDoneEvent event = converter.toObject(payload, OrderDoneEvent.class);
        orderService.updateOrderAsDone(event.getOrder().getId());
    }

    @RabbitListener(queues = {"${queue.order-done}"})
    public void handleOrderCanceledEvent(@Payload String payload) {
        log.debug("Handling a order canceled event {}", payload);
        OrderCanceledEvent event = converter.toObject(payload, OrderCanceledEvent.class);
        orderService.updateOrderAsCanceled(event.getOrder().getId());
    }
}
