package dev.lucasdeabreu.saga.refund;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private RefundStatus status;

    public enum RefundStatus {
        NEW, DONE, CANCEL
    }

    public Refund(Long orderId, RefundStatus status) {
        this.orderId = orderId;
        this.status = status;
    }
}