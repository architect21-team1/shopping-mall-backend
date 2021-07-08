package dev.lucasdeabreu.saga.order;

import dev.lucasdeabreu.saga.OrderException;
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

    // 주문 처리 - 정상
    @Transactional
    public void orderComplete(Long orderId) {
        Order order = updateOrderStatus(orderId, OrderStatus.DONE);
        repository.save(order);
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

    // 환불처리 - 정상
    @Transactional
    public void orderCancel(Refund refund) {
        try {
            Order order = updateOrderStatus(refund.getOrderId(), OrderStatus.CANCEL);

            publishOrderCancel(refund);

            repository.save(order);
        } catch(OrderException e) {
            log.error(e.getMessage());
        }
    }

    private Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        log.debug("Updating Order {} to {}", orderId, orderStatus);
        Optional<Order> orderOptional = repository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(orderStatus);
            return order;
        } else {
            throw new OrderException("Cannot update Order to status " + orderStatus + "Order " + orderId + " not found");
        }
    }

    private void publishOrderCancel(Refund refund) {
        OrderCancelEvent event = new OrderCancelEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing an order created event {}", event);
        publisher.publishEvent(event);
    }

    @Transactional
    public void orderCancel(Long orderId) {
        log.debug("Canceling Order {}", orderId);
        Optional<Order> optionalOrder = repository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.CANCEL);
            repository.save(order);
            log.debug("Order {} was canceled", order.getId());
        } else {
            log.error("Cannot find an Order by transaction {}", orderId);
        }
    }

}
