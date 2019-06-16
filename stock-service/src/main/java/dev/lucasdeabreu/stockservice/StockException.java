package dev.lucasdeabreu.stockservice;

public class StockException extends RuntimeException {
    public StockException(String message) {
        super(message);
    }
}
