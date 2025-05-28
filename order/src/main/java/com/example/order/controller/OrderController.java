package com.example.order.controller;

import com.example.order.WebClient.DTO.Payment;
import com.example.order.WebClient.DTO.Product;
import com.example.order.WebClient.InventoryClient;
import com.example.order.WebClient.PaymentClient;
import com.example.order.WebClient.ProductClient;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final InventoryClient inventoryClient;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderService orderService;

    public OrderController(OrderService orderService, InventoryClient inventoryClient, ProductClient productClient, PaymentClient paymentClient, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.inventoryClient = inventoryClient;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrderByID(@PathVariable("id") UUID id) {
        return Mono.fromCallable(()->orderService.findById(id).orElse(null));
    }

   @GetMapping
   public Flux<Order> getAllOrders() {
        return Flux.fromIterable(orderService.findAll());
   }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        String description = "Order payment";
        String transactionId = UUID.randomUUID().toString();

        return productClient.getAllProductsByIds(order.getProducts())
                .collectList()
                .flatMap(products -> {
                    double totalPrice = products.stream()
                            .mapToDouble(Product::price)
                            .sum();

                    BigDecimal amount = BigDecimal.valueOf(totalPrice);

                    Payment payment = new Payment(
                            UUID.randomUUID(),
                            description,
                            LocalDateTime.now(),
                            amount,
                            transactionId
                    );

                    return inventoryClient.checkInventory(order.getProducts())
                            .flatMap(inventoryAvailable -> {
                                if (!inventoryAvailable) {
                                    return Mono.error(new RuntimeException("Some products are out of stock."));
                                }

                                return paymentClient.savePayment(payment)
                                        .flatMap(savedPayment -> inventoryClient.updateInventories(order.getProducts())
                                                .collectList()
                                                .flatMap(updatedInventories -> {
                                                    Order newOrder = new Order(
                                                            UUID.randomUUID(),
                                                            LocalDate.now(),
                                                            order.getProducts(),
                                                            savedPayment.id()
                                                    );
                                                    return Mono.justOrEmpty(orderService.save(newOrder));
                                                }));
                            });
                });
    }




    @PutMapping("/{id}")
    public Mono<Order> updateOrder(@PathVariable("id") UUID id, @RequestBody Order order) {
        return Mono.fromCallable(()->orderService.updateOrder(id, order).orElse(null));
   }

   @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable("id") UUID id) {
        return Mono.fromRunnable(() -> orderService.deleteById(id));
   }

}
