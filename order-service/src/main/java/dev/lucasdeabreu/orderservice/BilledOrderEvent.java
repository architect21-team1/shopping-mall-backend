package dev.lucasdeabreu.orderservice;

import lombok.Data;

@Data
public class BilledOrderEvent {

    private Order order;

}
