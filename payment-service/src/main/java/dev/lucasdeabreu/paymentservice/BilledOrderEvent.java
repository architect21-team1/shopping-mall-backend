package dev.lucasdeabreu.paymentservice;

import dev.lucasdeabreu.paymentservice.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
@AllArgsConstructor
public class BilledOrderEvent {

    private final Order order;

}
