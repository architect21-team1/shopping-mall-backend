package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.payment.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailPreparedProductEvent {
    private String transactionId;
    private final Order order;
}
