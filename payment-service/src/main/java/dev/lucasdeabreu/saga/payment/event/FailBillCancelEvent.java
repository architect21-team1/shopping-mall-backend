package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.refund.Refund;
import lombok.*;

@Data
@AllArgsConstructor
public class FailBillCancelEvent {
    private String transactionId;
    private final Refund refund;
}
