package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.payment.Order;
import lombok.*;

@Data
@AllArgsConstructor
public class FailOrderCompleteEvent {
    private String transactionId;
    private final Order order;
}
