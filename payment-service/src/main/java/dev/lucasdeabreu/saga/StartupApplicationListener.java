package dev.lucasdeabreu.saga;

import dev.lucasdeabreu.saga.payment.PaymentStatus;
import dev.lucasdeabreu.saga.payment.repository.PaymentStatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final PaymentStatusRepository paymentStatusRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Creating payment status");
        log.debug("Created payment status status{}", paymentStatusRepository.save(new PaymentStatus("payment", PaymentStatus.Status.NORMAL)));
    }
}
