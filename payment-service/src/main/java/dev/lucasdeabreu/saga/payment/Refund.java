package dev.lucasdeabreu.saga.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Refund {

    private Long id;

    private Long orderId;

}