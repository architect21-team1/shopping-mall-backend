package dev.lucasdeabreu.saga.payment.event;

import dev.lucasdeabreu.saga.payment.Order;
import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
public class FailBillCompleteEvent {
    private String transactionId;
    private final Order order;
}
