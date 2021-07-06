package dev.lucasdeabreu.saga.stock.handler;

import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import dev.lucasdeabreu.saga.stock.ProductService;
import dev.lucasdeabreu.saga.stock.StockException;
import dev.lucasdeabreu.saga.stock.event.BillCancelEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
public class BillCancelHandler {

    private final Converter converter;
    private final ProductService productService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.bill-cancel}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a bill cancel event {}", payload);
        BillCancelEvent event = converter.toObject(payload, BillCancelEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        try {
            productService.cancelUpdateQuantityRefund(event.getRefund());
        } catch (StockException e) {
            log.error("Cannot update stock, reason: {}", e.getMessage());
        }
    }


}
