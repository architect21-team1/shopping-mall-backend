package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.payment.Order;
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
