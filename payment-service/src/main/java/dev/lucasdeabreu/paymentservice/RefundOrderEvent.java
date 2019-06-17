package dev.lucasdeabreu.paymentservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefundOrderEvent {
    private Order order;
    private String reason;
}
