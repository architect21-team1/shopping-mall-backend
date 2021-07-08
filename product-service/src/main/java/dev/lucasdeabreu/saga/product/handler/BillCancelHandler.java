package dev.lucasdeabreu.saga.product.handler;

import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import dev.lucasdeabreu.saga.product.StockService;
import dev.lucasdeabreu.saga.product.event.BillCancelEvent;
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
    private final StockService stockService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.bill-cancel}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a bill cancel event {}", payload);
        BillCancelEvent event = converter.toObject(payload, BillCancelEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        stockService.refundComplete(event.getRefund());
    }


}
