package dev.lucasdeabreu.saga.payment;

import dev.lucasdeabreu.saga.payment.event.BillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.BillCompleteEvent;
import dev.lucasdeabreu.saga.payment.event.FailBillCancelEvent;
import dev.lucasdeabreu.saga.payment.event.FailBillCompleteEvent;
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
        log.debug("Bill order {}", order);

        try {
            Payment payment = createOrder(order);

            checkPayment(order);

            log.debug("Saving payment {}", payment);
            paymentRepository.save(payment);

            publishBillComplete(order);
        } catch(PaymentException e) {
            log.error(e.getMessage());
            publishFailBillComplete(order);
        }
    }

    private void checkPayment(Order order) {
        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepository.findById("payment");
        if (paymentStatusOptional.isPresent()) {
            PaymentStatus paymentStatus = paymentStatusOptional.get();
            if (paymentStatus.getPaymentStatus().equals(PaymentStatus.Status.ORDER_PAYMENT_FAIL)) {
                throw new PaymentException("Payment - Order " + order.getId() + " have a problem.");
            }
        }
    }

    private void publishBillComplete(Order order) {
        BillCompleteEvent billedOrderEvent = new BillCompleteEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a bill complete event {}", billedOrderEvent);
        publisher.publishEvent(billedOrderEvent);
    }

    private void publishFailBillComplete(Order order) {
        FailBillCompleteEvent failBillCompleteEvent = new FailBillCompleteEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing a fail bill complete  event {}", failBillCompleteEvent);
        publisher.publishEvent(failBillCompleteEvent);
    }

    private Payment createOrder(Order order) {
        return Payment.builder()
                .paymentStatus(Payment.PaymentStatus.BILLED)
                .valueBilled(order.getValue())
                .orderId(order.getId())
                .build();
    }

    @Transactional
    public void billCancel(Refund refund) {
        log.debug("Bill cancel order {}", refund.getOrderId());
        try {
            Payment payment = cancelPayment(refund.getOrderId());

            checkCancelPayment(refund.getId(), refund.getOrderId(), payment.getId());

            paymentRepository.save(payment);

            publishBillCancel(refund);
        } catch (PaymentException e) {
            log.error(e.getMessage());
            publishFailBillCancel(refund);
        }
    }

    private Payment cancelPayment(Long orderId) {
        log.debug("Refund Payment by order id {}", orderId);
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(Payment.PaymentStatus.REFUND);
            return payment;
        } else {
            throw new PaymentException("Cannot find the Payment by order id " +  orderId + " to refund");
        }
    }

    private void checkCancelPayment(Long refundId, Long orderId, Long paymentId) {
        Optional<PaymentStatus> paymentStatusOptional = paymentStatusRepository.findById("payment");
        if (paymentStatusOptional.isPresent()) {
            PaymentStatus paymentStatus = paymentStatusOptional.get();
            if (paymentStatus.getPaymentStatus().equals(PaymentStatus.Status.REFUND_PAYMENT_FAIL)) {
                throw new PaymentException("[Refund] Payment [refundId: " + refundId + ", paymentId: " + paymentId + ", orderId: " + orderId + "] have a problem.");
            }
        }
    }

    private void publishBillCancel(Refund refund) {
        BillCancelEvent billCancelEvent = new BillCancelEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing a bill cancel event {}", billCancelEvent);
        publisher.publishEvent(billCancelEvent);
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
    public void billCancel(Long orderId) {

        log.debug("Refund Payment by order id {}", orderId);
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(Payment.PaymentStatus.REFUND);
            paymentRepository.save(payment);
            log.debug("Payment - order id {} was refund", orderId);
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
