package dev.lucasdeabreu.saga.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCanceledEvent {
    private String transactionId;
    private Order order;
}

