package dev.lucasdeabreu.saga.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BilledOrderEvent {
    private String transactionId;
    private final Order order;
}
