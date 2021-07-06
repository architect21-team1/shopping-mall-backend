package dev.lucasdeabreu.saga.payment.handler;

import dev.lucasdeabreu.saga.payment.PaymentService;
import dev.lucasdeabreu.saga.payment.event.BillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.RefundCreateEvent;
import dev.lucasdeabreu.saga.payment.event.RefundPaymentEvent;
import dev.lucasdeabreu.saga.refund.Refund;
import dev.lucasdeabreu.saga.shared.Converter;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class RefundCreateHandler {

    private final Converter converter;
    private final PaymentService paymentService;
    private final TransactionIdHolder transactionIdHolder;

    @RabbitListener(queues = {"${queue.refund-create}"})
    public void onRefundCreate(@Payload String payload) {
        log.debug("Handling a refund create event {}", payload);
        RefundCreateEvent event = converter.toObject(payload, RefundCreateEvent.class);
        transactionIdHolder.setCurrentTransactionId(event.getTransactionId());
        paymentService.refundPayment(event.getRefund());
    }


}
