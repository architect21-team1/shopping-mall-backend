package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Order createOrder(Order order) {
        order.setTransactionId(UUID.randomUUID().toString());

        publish(order);

        log.debug("Saving an order {}", order);
        return repository.save(order);
    }

    private void publish(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(order);
        log.debug("Publishing an order created event {}", event);
        publisher.publishEvent(event);
    }
}
