package dev.lucasdeabreu.stockservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCanceledEvent {
    private final Order order;
    private final String reason;
}
