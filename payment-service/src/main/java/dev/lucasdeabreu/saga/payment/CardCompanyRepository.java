package dev.lucasdeabreu.saga.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardCompanyRepository extends JpaRepository<Card, Long> {
}
