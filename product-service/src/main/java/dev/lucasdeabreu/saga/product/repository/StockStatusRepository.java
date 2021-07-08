package dev.lucasdeabreu.saga.product.repository;

import dev.lucasdeabreu.saga.product.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockStatusRepository extends JpaRepository<StockStatus, String> {
}
