package dev.lucasdeabreu.saga.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderDoneEvent {
    private String transactionId;
    private Order order;
}