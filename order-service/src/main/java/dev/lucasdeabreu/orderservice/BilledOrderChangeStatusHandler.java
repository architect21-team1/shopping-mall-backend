package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class BilledOrderChangeStatusHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = {"${queue.billed-order}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a billed order change status event {}", payload);
        BilledOrderEvent event = converter.toObject(payload, BilledOrderEvent.class);
        orderService.updateOrderAsBilled(event.getOrder().getId());
    }

}
