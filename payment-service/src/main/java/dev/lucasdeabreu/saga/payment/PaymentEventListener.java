package dev.lucasdeabreu.saga.payment;

import dev.lucasdeabreu.saga.payment.event.BillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.BillCompleteEvent;
import dev.lucasdeabreu.saga.payment.event.FailBillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.FailBillCompleteEvent;
import dev.lucasdeabreu.saga.shared.Converter;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Component
public class PaymentEventListener {

    private final static String ROUTING_KEY = "";

    private final RabbitTemplate rabbitTemplate;
    private final Converter converter;
    private final String queueBilledOrderName;
    private final String queueBillCancelName;
    private final String queueFailBillCompleteName;
    private final String queueFailPreparedProductName;

    public PaymentEventListener(RabbitTemplate rabbitTemplate,
                                Converter converter,
                                @Value("${queue.bill-complete}") String queueBilledOrderName,
                                @Value("${queue.bill-cancel}") String queueBillCancelName,
                                @Value("${queue.fail-bill-complete}") String queueFailBillCompleteName,
                                @Value("${queue.fail-prepared-product}") String queueFailPreparedProductName) {
        this.rabbitTemplate = rabbitTemplate;
        this.converter = converter;
        this.queueBilledOrderName = queueBilledOrderName;
        this.queueBillCancelName = queueBillCancelName;
        this.queueFailBillCompleteName = queueFailBillCompleteName;
        this.queueFailPreparedProductName = queueFailPreparedProductName;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBilledOrderEvent(BillCompleteEvent event) {
        log.debug("Sending billed order event to {}, event: {}", queueBilledOrderName, event);
        rabbitTemplate.convertAndSend(queueBilledOrderName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBillCancelEvent(BillCancelEvent event) {
        log.debug("Sending billed cancel event to {}, event: {}", queueBillCancelName, event);
        rabbitTemplate.convertAndSend(queueBillCancelName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onFailPreparedProductEvent(FailBillCompleteEvent event) {
        log.debug("Sending fail prepared product event to {}, event: {}", queueFailPreparedProductName, event);
        rabbitTemplate.convertAndSend(queueFailPreparedProductName, converter.toJSON(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onFailBillCompleteEvent(FailBillCancelEvent event) {
        log.debug("Sending fail bill complete event to {}, event: {}", queueFailBillCompleteName, event);
        rabbitTemplate.convertAndSend(queueFailBillCompleteName, converter.toJSON(event));
    }
}
