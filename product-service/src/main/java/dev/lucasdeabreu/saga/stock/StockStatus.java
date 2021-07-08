package dev.lucasdeabreu.saga.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stockStatus")
public class StockStatus {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Status stockStatus;

    public enum Status {
        NORMAL, ORDER_STOCK_FAIL, REFUND_STOCK_FAIL
    }
}
