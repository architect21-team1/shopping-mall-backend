package dev.lucasdeabreu.saga.product.handler;

import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import dev.lucasdeabreu.saga.product.StockService;
import dev.lucasdeabreu.saga.product.StockException;
import dev.lucasdeabreu.saga.product.event.BillCompleteEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
public class BillCompleteHandler {

    private final Converter converter;
    private final StockService stockService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.bill-complete}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a bill complete event {}", payload);
        BillCompleteEvent event = converter.toObject(payload, BillCompleteEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        try {
            stockService.updateQuantity(event.getOrder());
        } catch (StockException e) {
            log.error("Cannot update stock, reason: {}", e.getMessage());
        }
    }
}
