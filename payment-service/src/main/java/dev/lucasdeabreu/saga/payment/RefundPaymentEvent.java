package dev.lucasdeabreu.saga.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefundPaymentEvent {
    private String transactionId;
    private Order order;
}
