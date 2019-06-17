package dev.lucasdeabreu.orderservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderDoneEvent {
    private Order order;
}