package dev.lucasdeabreu.saga.order.handler;

import dev.lucasdeabreu.saga.order.OrderService;
import dev.lucasdeabreu.saga.order.event.OrderCompleteEvent;
import dev.lucasdeabreu.saga.shared.Converter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class OrderCompleteEventHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = {"${queue.order-complete}"})
    public void handleOrderDoneEvent(@Payload String payload) {
        log.debug("Handling a order complete event {}", payload);
        OrderCompleteEvent event = converter.toObject(payload, OrderCompleteEvent.class);
        orderService.updateOrderAsDone(event.getOrder().getId());
    }
}
