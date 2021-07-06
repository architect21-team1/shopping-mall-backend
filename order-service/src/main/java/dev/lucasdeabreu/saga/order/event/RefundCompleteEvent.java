package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RefundCompleteEvent {
    private String transactionId;
    private Refund refund;
}
