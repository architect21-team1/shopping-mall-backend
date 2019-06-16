package dev.lucasdeabreu.stockservice;

import lombok.Data;

@Data
public class BilledOrderEvent {

    private Order order;

}
