package dev.lucasdeabreu.saga.payment.handler;

import dev.lucasdeabreu.saga.payment.PaymentService;
import dev.lucasdeabreu.saga.payment.event.RefundOrderEvent;
import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class RefundOrderHandler {

    private final Converter converter;
    private final PaymentService paymentService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.refund-order}"})
    public void onRefundCreate(@Payload String payload) {
        log.debug("Handling a refund create event {}", payload);
        RefundOrderEvent event = converter.toObject(payload, RefundOrderEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        paymentService.billCancel(event.getRefund());
    }


}
