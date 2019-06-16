package dev.lucasdeabreu.stockservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDoneEvent {
    private final Order order;
}
