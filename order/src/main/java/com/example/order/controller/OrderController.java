package com.example.order.controller;

import com.example.order.dto.BaseResponse;
import com.example.order.entity.Order;
import com.example.order.exceptions.OutOfStockException;
import com.example.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final WebClient.Builder webClientBuilder;

    public OrderController(OrderService orderService, WebClient.Builder webClientBuilder) {
        this.orderService = orderService;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrderByID(@PathVariable("id") UUID id) {
        return Mono.fromCallable(() -> orderService.findById(id).orElse(null));
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return Flux.fromIterable(orderService.findAll());
    }


    @PostMapping
    public Mono<Order> createOrder(@RequestBody  Order order) {
        return this.webClientBuilder.build()
                .post()
                .uri("http://inventory/api/inventories/in-stock")
                .bodyValue(order.getProducts())
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .flatMap(result -> {
                    if (result != null && !result.hasError()) {
                            return Mono.fromCallable(()-> orderService.save(order));
                    } else {
                        return Mono.error(new OutOfStockException("Some products are not in stock"));
                    }
                });

    }



    @PutMapping("/{id}")
    public Mono<Order> updateOrder(@PathVariable("id") UUID id, @RequestBody Order order) {
        return Mono.fromCallable(() -> orderService.updateOrder(id, order).orElse(null));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(@PathVariable("id") UUID id) {
        return Mono.fromRunnable(() -> orderService.deleteById(id));
    }
}
