package dev.lucasdeabreu.saga.payment;

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
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;
    @PostMapping("/status")
    public ResponseEntity updatePaymentStatus(@RequestBody PaymentStatus.Status status) {
        log.debug("Updating a Payment status to {}", status);
        service.updatePaymentStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
