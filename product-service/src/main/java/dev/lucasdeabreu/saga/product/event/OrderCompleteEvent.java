package dev.lucasdeabreu.saga.product.event;

import dev.lucasdeabreu.saga.product.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCompleteEvent {
    private String transactionId;
    private final Order order;
}
