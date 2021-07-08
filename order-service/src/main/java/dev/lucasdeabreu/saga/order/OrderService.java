package dev.lucasdeabreu.saga.order;

import dev.lucasdeabreu.saga.order.Order.OrderStatus;
import dev.lucasdeabreu.saga.order.event.OrderCancelEvent;
import dev.lucasdeabreu.saga.order.event.OrderCreatedEvent;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdHolder transactionIdHolder;

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.NEW);

        publish(order);

        log.debug("Saving an order {}", order);
        return repository.save(order);
    }

    private void publish(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(UUID.randomUUID().toString(), order);
        log.debug("Publishing an order created event {}", event);
        publisher.publishEvent(event);
    }

    public List<Order> getAll() {
        return repository.findAll();
    }

    public Order getOrder(Long orderId) {
        Optional<Order> order = repository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            log.error("Cannot update Order to status {}, Order {} not found", OrderStatus.DONE, orderId);
            return null;
        }
    }

    @Transactional
    public void updateOrderAsDone(Long orderId) {
        log.debug("Updating Order {} to {}", orderId, OrderStatus.DONE);
        Optional<Order> orderOptional = repository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.DONE);
            repository.save(order);
        } else {
            log.error("Cannot update Order to status {}, Order {} not found", OrderStatus.DONE, orderId);
        }
    }

    @Transactional
    public void cancelOrderRefund(Refund refund) {
        cancelOrder(refund.getOrderId());
        publishOrderCancel(refund);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        log.debug("Canceling Order {}", orderId);
        Optional<Order> optionalOrder = repository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.CANCELED);
            repository.save(order);
            log.debug("Order {} was canceled", order.getId());
        } else {
            log.error("Cannot find an Order by transaction {}", orderId);
        }
    }

    private void publishOrderCancel(Refund refund) {
        OrderCancelEvent event = new OrderCancelEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing an order cancel event {}", event);
        publisher.publishEvent(event);
    }
}
