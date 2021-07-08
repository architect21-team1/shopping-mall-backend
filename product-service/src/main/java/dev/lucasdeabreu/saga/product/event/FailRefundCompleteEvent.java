package dev.lucasdeabreu.saga.product.event;

import dev.lucasdeabreu.saga.product.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FailRefundCompleteEvent {
    private String transactionId;
    private Refund refund;
}
