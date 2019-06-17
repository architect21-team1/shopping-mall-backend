package dev.lucasdeabreu.orderservice;

import dev.lucasdeabreu.orderservice.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderCreatedEvent {
    private Order order;
}
