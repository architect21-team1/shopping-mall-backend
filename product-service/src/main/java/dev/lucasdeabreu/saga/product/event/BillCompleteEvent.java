package dev.lucasdeabreu.saga.product.event;

import dev.lucasdeabreu.saga.product.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BillCompleteEvent {
    private String transactionId;
    private Order order;
}