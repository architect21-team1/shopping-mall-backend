package dev.lucasdeabreu.paymentservice.order;

import lombok.Data;

@Data
public class OrderCreatedEvent {

    private Order order;

}
