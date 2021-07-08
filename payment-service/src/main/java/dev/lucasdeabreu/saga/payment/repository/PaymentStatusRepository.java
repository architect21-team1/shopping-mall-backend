package dev.lucasdeabreu.saga.payment.repository;

import dev.lucasdeabreu.saga.payment.Payment;
import dev.lucasdeabreu.saga.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, String> {
}
