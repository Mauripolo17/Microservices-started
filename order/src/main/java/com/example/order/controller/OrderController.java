package com.example.order.controller;

import com.example.order.entity.Order;
import com.example.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
        return Mono.fromCallable(() -> orderService.save(order));
   }

   @PutMapping("{id}")
    public Mono<Order> updateOrder(@PathVariable("id") UUID id, @RequestBody Order order) {
        return Mono.fromCallable(()->orderService.updateOrder(id, order).orElse(null));
   }

   @DeleteMapping("{id]")
    public Mono<Void> deleteOrder(@PathVariable("id") UUID id) {
        return Mono.fromRunnable(() -> orderService.deleteById(id));
   }

}
