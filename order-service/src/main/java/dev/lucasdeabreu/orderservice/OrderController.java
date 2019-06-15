package dev.lucasdeabreu.orderservice;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity create(@RequestBody Order order) {
        log.debug("Post an order {}", order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createOrder(order));
    }
}
