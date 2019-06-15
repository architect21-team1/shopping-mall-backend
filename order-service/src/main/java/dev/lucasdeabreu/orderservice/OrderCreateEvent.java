package dev.lucasdeabreu.orderservice;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class OrderCreateEvent {

    private final String transactionId;
    private final Order order;

    public OrderCreateEvent(Order order) {
        this.order = order;
        this.transactionId = UUID.randomUUID().toString();
    }
}
