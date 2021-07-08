package dev.lucasdeabreu.saga.stock.event;

import dev.lucasdeabreu.saga.stock.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCompleteEvent {
    private String transactionId;
    private final Order order;
}
