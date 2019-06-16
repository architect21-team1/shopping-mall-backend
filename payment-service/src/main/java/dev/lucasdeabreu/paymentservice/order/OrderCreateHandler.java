package dev.lucasdeabreu.paymentservice.order;

import dev.lucasdeabreu.paymentservice.Converter;
import dev.lucasdeabreu.paymentservice.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class OrderCreateHandler {

    private final Converter converter;
    private final PaymentService paymentService;

    @RabbitListener(queues = {"${queue.order-create}"})
    public void handle(@Payload String orderCreateEventPayload) {
        log.debug("Handling a created order event {}", orderCreateEventPayload);
        OrderCreatedEvent event = converter.toObject(orderCreateEventPayload, OrderCreatedEvent.class);
        paymentService.charge(event.getOrder());
    }

}
