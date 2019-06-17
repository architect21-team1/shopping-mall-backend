package dev.lucasdeabreu.paymentservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefundPaymentEvent {
    private Order order;
    private String reason;
}
