package dev.lucasdeabreu.saga.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderCreatedEvent {
    private String transactionId;
    private Order order;
}
