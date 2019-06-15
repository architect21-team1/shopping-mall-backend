package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Order createOrder(Order order) {
        OrderCreateEvent event = new OrderCreateEvent(order);
        log.debug("Publish an order {}", event);
        publisher.publishEvent(event);
        log.debug("Saving an order {}", order);
        return repository.save(order);
    }
}
