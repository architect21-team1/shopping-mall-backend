package dev.lucasdeabreu.saga.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long quantity;
    private String picture;
    private BigDecimal price;

    public Product(String name, Long quantity, String picture, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.picture = picture;
        this.price = price;
    }
}
