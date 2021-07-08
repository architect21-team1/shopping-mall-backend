package dev.lucasdeabreu.saga.stock.repository;

import dev.lucasdeabreu.saga.stock.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
