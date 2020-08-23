package dev.lucasdeabreu.saga.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {

    private Long id;

    private Long productId;

    private BigDecimal value;

    private Long quantity;
}