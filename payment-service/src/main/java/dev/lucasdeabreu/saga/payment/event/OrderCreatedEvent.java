package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.payment.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderCreatedEvent {
    private String transactionId;
    private Order order;
}