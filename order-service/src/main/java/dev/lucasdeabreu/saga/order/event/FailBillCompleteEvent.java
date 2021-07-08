package dev.lucasdeabreu.saga.order.event;

import dev.lucasdeabreu.saga.order.Order;
import dev.lucasdeabreu.saga.order.Refund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FailBillCompleteEvent {
    private String transactionId;
    private Order order;
}
