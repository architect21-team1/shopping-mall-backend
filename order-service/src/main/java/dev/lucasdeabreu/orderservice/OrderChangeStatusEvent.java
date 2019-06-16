package dev.lucasdeabreu.orderservice;

import lombok.Data;

@Data
public class OrderChangeStatusEvent {

    private Order order;

}
