package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelEvent {
    private String transactionId;
    private Refund refund;
}

