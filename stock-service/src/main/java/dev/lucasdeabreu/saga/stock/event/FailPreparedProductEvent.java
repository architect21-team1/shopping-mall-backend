package dev.lucasdeabreu.saga.stock.event;

import dev.lucasdeabreu.saga.stock.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FailPreparedProductEvent {
    private String transactionId;
    private Order order;
}