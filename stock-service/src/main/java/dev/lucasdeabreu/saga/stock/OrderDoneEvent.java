package dev.lucasdeabreu.saga.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDoneEvent {
    private String transactionId;
    private final Order order;
}
