package dev.lucasdeabreu.saga.stock;

import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import dev.lucasdeabreu.saga.stock.event.OrderCanceledEvent;
import dev.lucasdeabreu.saga.stock.event.OrderDoneEvent;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdHolder transactionIdHolder;

    @Transactional
    public void updateQuantity(Order order) {
        log.debug("Start updating product {}", order.getProductId());

        Product product = getProduct(order);
        checkStock(order, product);
        updateStock(order, product);

        publishOrderDone(order);
    }

    @Transactional
    public void cancelUpdateQuantity(Order order) {
        log.debug("Start updating product {}", order.getProductId());

        Product product = getProduct(order);
        cancelUpdateStock(order, product);

        publishOrderCancel(order);
    }

    private void cancelUpdateStock(Order order, Product product) {
        product.setQuantity(product.getQuantity() + order.getQuantity());
        log.debug("Canceling updated product {} with quantity {}", product.getId(), product.getQuantity());
        productRepository.save(product);
        publishOrderCancel(order);
    }

    private void updateStock(Order order, Product product) {
        product.setQuantity(product.getQuantity() - order.getQuantity());
        log.debug("Updating product {} with quantity {}", product.getId(), product.getQuantity());
        productRepository.save(product);
    }

    private void publishOrderDone(Order order) {
        OrderDoneEvent event = new OrderDoneEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing order done event {}", event);
        publisher.publishEvent(event);
    }

    private void publishOrderCancel(Order order) {
        OrderCanceledEvent event = new OrderCanceledEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing order cancel event {}", event);
        publisher.publishEvent(event);
    }

    private void checkStock(Order order, Product product) {
        log.debug("Checking, products available {}, products ordered {}", product.getQuantity(), order.getQuantity());
        if (product.getQuantity() < order.getQuantity()) {
            publishCanceledOrder(order);
            throw new StockException("Product " + product.getId() + " is out of stock");
        }
    }

    private void publishCanceledOrder(Order order) {
        OrderCanceledEvent event = new OrderCanceledEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing canceled order event {}", event);
        publisher.publishEvent(event);
    }

    private Product getProduct(Order order) {
        Optional<Product> optionalProduct = productRepository.findById(order.getProductId());
        return optionalProduct.orElseThrow(() -> {
            publishCanceledOrder(order);
            return new StockException("Cannot find a product " + order.getProductId());
        });
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product get(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
