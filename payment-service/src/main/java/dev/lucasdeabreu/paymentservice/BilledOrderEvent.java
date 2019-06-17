package dev.lucasdeabreu.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BilledOrderEvent {
    private final Order order;
}
