package dev.lucasdeabreu.saga;

import dev.lucasdeabreu.saga.product.Product;
import dev.lucasdeabreu.saga.product.StockStatus;
import dev.lucasdeabreu.saga.product.repository.ProductRepository;
import dev.lucasdeabreu.saga.product.repository.StockStatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Log4j2
@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final StockStatusRepository stockStatusRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Creating default products");
        log.debug("Created product {}", productRepository.save(new Product("반지", 3L, "https://media.tiffany.com/is/image/tiffanydm/03_Desktop_1440x924_PersonalizeYourEngagementRingAndBox?$tile$&wid=1440&hei=912", new BigDecimal(100))));
        log.debug("Created product {}", productRepository.save(new Product("시계", 4L, "https://media.tiffany.com/is/image/tiffanydm/T_LE_Watches_2x2MktgTile2?$tile$&wid=1488&hei=1488", new BigDecimal(200))));
        log.debug("Created product {}", productRepository.save(new Product("목걸이", 10L, "https://media.tiffany.com/is/image/Tiffany/EcomBrowseM/tiffany-1837-23579197_907511_ED-23579197_907511_ED.jpg?hei=720&wid=720", new BigDecimal(300))));

        log.debug("Creating default stock status");
        log.debug("Created product {}", stockStatusRepository.save(new StockStatus("stock", StockStatus.Status.NORMAL)));
    }
}
