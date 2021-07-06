package dev.lucasdeabreu.saga.payment;

import dev.lucasdeabreu.saga.payment.event.BillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.BilledOrderEvent;
import dev.lucasdeabreu.saga.payment.event.FailPreparedProductEvent;
import dev.lucasdeabreu.saga.refund.Refund;
import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardCompanyRepository cardCompanyRepository;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdHolder transactionIdHolder;

    @Transactional
    public void charge(Order order) {
        log.debug("Charging order {}", order);

        /*
         * Any business logic to confirm charge
         * ...
         */

        Payment payment = createOrder(order);

//        checkPayment(order, payment);

        log.debug("Saving payment {}", payment);
        paymentRepository.save(payment);

        publish(order);
    }

    private void checkPayment(Order order, Payment payment) {
        publishFailPreparedProduct(order);
        throw new PaymentException("Payment " + payment.getId() + " have a problem.");
    }

    private void publishFailPreparedProduct(Order order) {
        FailPreparedProductEvent failPreparedProductEvent = new FailPreparedProductEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a fail payment event {}", failPreparedProductEvent);
        publisher.publishEvent(failPreparedProductEvent);
    }

    private void publish(Order order) {
        BilledOrderEvent billedOrderEvent = new BilledOrderEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a billed order event {}", billedOrderEvent);
        publisher.publishEvent(billedOrderEvent);
    }

    private Payment createOrder(Order order) {
        return Payment.builder()
                .paymentStatus(Payment.PaymentStatus.BILLED)
                .valueBilled(order.getValue())
                .orderId(order.getId())
                .build();
    }

    @Transactional
    public void refundPayment(Refund refund) {
        refund(refund.getOrderId());
        publish(refund);
    }

    private void publish(Refund refund) {
        BillCancelEvent event = new BillCancelEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing an bill cancel event {}", event);
        publisher.publishEvent(event);
    }

    @Transactional
    public void refund(Long orderId) {
        log.debug("Refund Payment by order id {}", orderId);
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(Payment.PaymentStatus.REFUND);
            paymentRepository.save(payment);
            log.debug("Payment {} was refund", payment.getId());
        } else {
            log.error("Cannot find the Payment by order id {} to refund", orderId);
        }
    }

}
