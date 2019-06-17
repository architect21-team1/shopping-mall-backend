package dev.lucasdeabreu.paymentservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderCreatedEvent {
    private Order order;
}