package dev.lucasdeabreu.saga.stock.repository;

import dev.lucasdeabreu.saga.stock.Product;
import dev.lucasdeabreu.saga.stock.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockStatusRepository extends JpaRepository<StockStatus, String> {
}
