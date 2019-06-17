package dev.lucasdeabreu.orderservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCanceledEvent {
    private Order order;
    private String reason;
}

