package dev.lucasdeabreu.saga.stock;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.get(id));
    }

    @PostMapping("/status")
    public ResponseEntity updateStockStatus(@RequestBody StockStatus.Status status) {
        log.debug("Updating a Payment status to {}", status);
        service.updateStockStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
