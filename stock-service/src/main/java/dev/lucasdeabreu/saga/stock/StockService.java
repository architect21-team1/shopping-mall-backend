package dev.lucasdeabreu.saga.stock;

import dev.lucasdeabreu.saga.shared.TransactionIdHolder;
import dev.lucasdeabreu.saga.stock.event.OrderCanceledEvent;
import dev.lucasdeabreu.saga.stock.event.OrderCompleteEvent;
import dev.lucasdeabreu.saga.stock.event.RefundCompleteEvent;
import dev.lucasdeabreu.saga.stock.repository.ProductRepository;
import dev.lucasdeabreu.saga.stock.repository.StockStatusRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockStatusRepository stockStatusRepository;
    private final ApplicationEventPublisher publisher;
    private final TransactionIdHolder transactionIdHolder;

    @Transactional
    public void updateQuantity(Order order) {
        try {
            log.debug("Start updating product {}", order.getProductId());

            Product product = getProduct(order);

            checkStock(product);
            updateStock(order, product);

            publishOrderComplete(order);
        } catch(StockException e) {
            log.error(e.getMessage());
        }
    }

    private void checkStock(Product product) {
        Optional<StockStatus> stockStatusOptional = stockStatusRepository.findById("stock");
        if (stockStatusOptional.isPresent()) {
            StockStatus stockStatus = stockStatusOptional.get();
            if (stockStatus.getStockStatus() == StockStatus.Status.ORDER_STOCK_FAIL) {
                throw new StockException("Product " + product.getId() + " have a problem.");
            }
        }
    }

    private void updateStock(Order order, Product product) {
        product.setQuantity(product.getQuantity() - order.getQuantity());
        log.debug("Updating product {} with quantity {}", product.getId(), product.getQuantity());
        productRepository.save(product);
    }

    private void publishOrderComplete(Order order) {
        OrderCompleteEvent event = new OrderCompleteEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing order done event {}", event);
        publisher.publishEvent(event);
    }

    @Transactional
    public void cancelUpdateQuantityOrder(Order order) {
        log.debug("Start updating product {}", order.getProductId());

        cancelUpdateQuantity(order);

        publishOrderCancel(order);
    }

    @Transactional
    public void cancelUpdateQuantityRefund(Refund refund) {
        try {
            Order order = getOrder(refund.getOrderId());
            cancelUpdateQuantity(order);
            publishRefundComplete(refund);
        } catch(HttpServerErrorException e) {
            log.error(e.getMessage());
        }
    }

    public void cancelUpdateQuantity(Order order) {
        Product product = getProduct(order);
        cancelUpdateStock(order, product);
    }

    public void occurError() {
//        throw new StockException();
    }

    private void publishRefundComplete(Refund refund) {
        RefundCompleteEvent event = new RefundCompleteEvent(transactionIdHolder.getCurrentTransactionId(), refund);
        log.debug("Publishing refund complete event {}", event);
        publisher.publishEvent(event);
    }

    private void cancelUpdateStock(Order order, Product product) {
        product.setQuantity(product.getQuantity() + order.getQuantity());
        log.debug("Canceling updated product {} with quantity {}", product.getId(), product.getQuantity());
        productRepository.save(product);
        publishOrderCancel(order);
    }

    private void publishOrderCancel(Order order) {
        OrderCanceledEvent event = new OrderCanceledEvent(transactionIdHolder.getCurrentTransactionId(), order);
        log.debug("Publishing order cancel event {}", event);
        publisher.publishEvent(event);
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

    private Order getOrder(Long orderId) {
        final String uri = "http://localhost:8081/api/orders/" + orderId ;
        log.info(uri);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Order.class);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product get(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void updateStockStatus(StockStatus.Status status) {
        Optional<StockStatus> paymentOptional = stockStatusRepository.findById("stock");
        if (paymentOptional.isPresent()) {
            StockStatus stockStatus = paymentOptional.get();
            stockStatus.setStockStatus(status);
            stockStatusRepository.save(stockStatus);
            log.debug("Stock Status was updated : {}", status);
        }
    }
}
