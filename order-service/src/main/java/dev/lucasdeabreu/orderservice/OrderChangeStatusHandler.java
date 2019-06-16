package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class OrderChangeStatusHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = {"${queue.billed-order}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a billed order change status event {}", payload);
        OrderChangeStatusEvent event = converter.toObject(payload, OrderChangeStatusEvent.class);
        orderService.updateOrderAsBilled(event.getOrder().getId());
    }

    @RabbitListener(queues = {"${queue.order-done}"})
    public void handleOrderDoneEvent(@Payload String payload) {
        log.debug("Handling a order done change status event {}", payload);
        OrderChangeStatusEvent event = converter.toObject(payload, OrderChangeStatusEvent.class);
        orderService.updateOrderAsDone(event.getOrder().getId());
    }

}
