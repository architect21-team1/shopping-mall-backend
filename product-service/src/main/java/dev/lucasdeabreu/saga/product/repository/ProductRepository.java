package dev.lucasdeabreu.saga.product.repository;

import dev.lucasdeabreu.saga.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
