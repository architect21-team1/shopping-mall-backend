package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class OrderCreatedEvent {

    private final Order order;

}
