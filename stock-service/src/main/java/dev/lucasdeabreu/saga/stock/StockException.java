package dev.lucasdeabreu.saga.stock;

public class StockException extends RuntimeException {
    public StockException(String message) {
        super(message);
    }
}
