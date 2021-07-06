package dev.lucasdeabreu.saga.order;

import lombok.Data;

@Data
public class Refund {

    private Long id;

    private Long orderId;

}