package dev.lucasdeabreu.saga.stock.handler;

import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.stock.StockService;
import dev.lucasdeabreu.saga.stock.StockException;
import dev.lucasdeabreu.saga.stock.event.FailPreparedProductEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
public class FailPreparedProductHandler {

    private final Converter converter;
    private final StockService stockService;

    @RabbitListener(queues = {"${queue.fail-prepared-product}"})
    public void handle(@Payload String payload) {
        log.debug("Handling a fail prepared product event {}", payload);
        FailPreparedProductEvent event = converter.toObject(payload, FailPreparedProductEvent.class);
        try {
            stockService.cancelUpdateQuantityOrder(event.getOrder());
        } catch (StockException e) {
            log.error("Cannot update stock, reason: {}", e.getMessage());
        }
    }
}
