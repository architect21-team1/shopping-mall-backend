package dev.lucasdeabreu.saga.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCanceledEvent {
    private String transactionId;
    private final Order order;
}
