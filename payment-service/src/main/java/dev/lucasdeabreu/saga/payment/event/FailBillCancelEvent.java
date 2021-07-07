package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.refund.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FailBillCancelEvent {
    private String transactionId;
    private Refund refund;
}
