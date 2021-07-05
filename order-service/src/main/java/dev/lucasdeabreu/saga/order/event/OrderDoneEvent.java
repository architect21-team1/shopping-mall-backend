package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderDoneEvent {
    private String transactionId;
    private Order order;
}