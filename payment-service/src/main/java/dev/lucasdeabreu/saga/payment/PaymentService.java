package dev.lucasdeabreu.saga.payment;

import dev.lucasdeabreu.saga.payment.event.BillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.BilledOrderEvent;
import dev.lucasdeabreu.saga.payment.event.FailBillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.FailPreparedProductEvent;
import dev.lucasdeabreu.saga.payment.repository.PaymentRepository;
import dev.lucasdeabreu.saga.payment.repository.PaymentStatusRepository;
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
    private final PaymentStatusRepository paymentStatusRepository;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdHolder transactionIdHolder;

    @Transactional
    public void bill(Order order) {
        log.debug("Charging order {}", order);

        try {
            Payment payment = createOrder(order);

            checkPayment(payment);

            log.debug("Saving payment {}", payment);
            paymentRepository.save(payment);

            publishBillComplete(order);
        } catch(PaymentException e) {
            log.error(e.getMessage());
            publishFailPreparedProduct(order);
        }
    }

    private void checkPayment(Payment payment) {
        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepository.findById("payment");
        if (paymentStatusOptional.isPresent()) {
            PaymentStatus paymentStatus = paymentStatusOptional.get();
            if (paymentStatus.getPaymentStatus().equals(PaymentStatus.Status.ORDER_PAYMENT_FAIL)) {
                throw new PaymentException("Payment " + payment.getId() + " have a problem.");
            }
        }
    }

    private void publishBillComplete(Order order) {
        BilledOrderEvent billedOrderEvent = new BilledOrderEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a bill complete event {}", billedOrderEvent);
        publisher.publishEvent(billedOrderEvent);
    }

    private void publishFailPreparedProduct(Order order) {
        FailPreparedProductEvent failPreparedProductEvent = new FailPreparedProductEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a fail payment event {}", failPreparedProductEvent);
        publisher.publishEvent(failPreparedProductEvent);
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
        try {
            occurError(refund);
            refund(refund.getOrderId());
            publishBillComplete(refund);
        } catch (PaymentException e) {
            log.error(e.getMessage() + "///");
            publishFailBillCancel(refund);
        }
    }

    private void occurError(Refund refund) {
        throw new PaymentException("Refund id " + refund.getId() + " have a problem.");
    }

    private void publishFailBillCancel(Refund refund) {
        FailBillCancelEvent failBillCancelEvent = new FailBillCancelEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing a fail bill cancel event {}", failBillCancelEvent);
        publisher.publishEvent(failBillCancelEvent);
    }

    private void publishBillComplete(Refund refund) {
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

    public void updatePaymentStatus(PaymentStatus.Status status) {
        Optional<PaymentStatus> paymentOptional = paymentStatusRepository.findById("payment");
        if (paymentOptional.isPresent()) {
            PaymentStatus paymentStatus = paymentOptional.get();
            paymentStatus.setPaymentStatus(status);
            paymentStatusRepository.save(paymentStatus);
            log.debug("Payment Status was updated : {}", status);
        }
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
