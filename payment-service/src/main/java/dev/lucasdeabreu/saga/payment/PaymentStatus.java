package dev.lucasdeabreu.saga.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "paymentStatus")
public class PaymentStatus {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Status paymentStatus;

    public enum Status {
        NORMAL, ORDER_PAYMENT_FAIL, REFUND_PAYMENT_FAIL
    }

}
