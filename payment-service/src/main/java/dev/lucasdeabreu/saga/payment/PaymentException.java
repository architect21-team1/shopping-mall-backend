package dev.lucasdeabreu.saga.payment;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
