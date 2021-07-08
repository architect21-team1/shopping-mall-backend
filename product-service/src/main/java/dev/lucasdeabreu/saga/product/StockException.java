package dev.lucasdeabreu.saga.product;

public class StockException extends RuntimeException {
    public StockException(String message) {
        super(message);
    }
}
