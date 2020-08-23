package dev.lucasdeabreu.saga.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BilledOrderEvent {
    private String transactionId;
    private Order order;
}