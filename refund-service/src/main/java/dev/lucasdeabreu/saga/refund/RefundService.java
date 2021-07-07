package dev.lucasdeabreu.saga.refund;

import dev.lucasdeabreu.saga.refund.event.RefundCreateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Service
public class RefundService {

    private final RefundRepository repository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Refund createRefund(Order order) {
        Refund refund = new Refund(order.getId(), Refund.RefundStatus.NEW);

        publish(refund);

        log.debug("Saving an refund {}", order);
        return repository.save(refund);
    }

    @Transactional
    public Refund completeRefund(Refund refund) {
        refund.setStatus(Refund.RefundStatus.DONE);
        return repository.save(refund);
    }

    private void publish(Refund refund) {
        RefundCreateEvent event = new RefundCreateEvent(UUID.randomUUID().toString(), refund);
        log.debug("Publishing an order created refund {}", event);
        publisher.publishEvent(event);
    }

    public List<Refund> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void updateOrderAsDone(Long refundId) {
        log.debug("Updating Refund {} to {}", refundId, Refund.RefundStatus.DONE);
        Optional<Refund> refundOptional = repository.findById(refundId);
        if (refundOptional.isPresent()) {
            Refund refund = refundOptional.get();
            refund.setStatus(Refund.RefundStatus.DONE);
            repository.save(refund);
        } else {
            log.error("Cannot update Order to status {}, Order {} not found", Refund.RefundStatus.DONE, refundId);
        }
    }

    public void cancelRefund(Refund refund) {
        log.debug("Updating Refund {} to {}", refund.getId(), Refund.RefundStatus.CANCEL);
        Optional<Refund> refundOptional = repository.findById(refund.getId());
        if (refundOptional.isPresent()) {
            Refund refundResult = refundOptional.get();
            refundResult.setStatus(Refund.RefundStatus.CANCEL);
            repository.save(refundResult);
        } else {
            log.error("Cannot update Order to status {}, Order {} not found", Refund.RefundStatus.CANCEL, refund.getId());
        }
    }
}
