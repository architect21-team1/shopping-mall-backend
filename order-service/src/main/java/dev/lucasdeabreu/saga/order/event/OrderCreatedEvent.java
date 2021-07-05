package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderCreatedEvent {
    private String transactionId;
    private Order order;
}
