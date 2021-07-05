package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCanceledEvent {
    private String transactionId;
    private Order order;
}

