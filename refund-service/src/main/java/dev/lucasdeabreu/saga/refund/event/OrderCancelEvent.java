package dev.lucasdeabreu.saga.refund.event;

import dev.lucasdeabreu.saga.refund.Refund;
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

