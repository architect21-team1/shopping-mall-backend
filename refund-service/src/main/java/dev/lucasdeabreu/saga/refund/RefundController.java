package dev.lucasdeabreu.saga.refund;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService service;

    @PostMapping
    public ResponseEntity<Refund> create(@RequestBody Order order) {
        log.debug("Creating a new {}", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRefund(order));
    }

    @GetMapping
    public ResponseEntity<List<Refund>> getAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
}
