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
        return Mono.justOrEmpty(orderService.findById(id));
    }

   @GetMapping
   public Flux<List<Order>> getAllOrders() {
        return Flux.just(orderService.findAll());
   }

   @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return Mono.justOrEmpty(orderService.save(order));
   }

   @PutMapping("{id}")
    public Mono<Order> updateOrder(@PathVariable("id") UUID id, @RequestBody Order order) {
        return Mono.justOrEmpty(orderService.save(order));
   }

   @DeleteMapping("{id]")
    public Mono<Void> deleteOrder(@PathVariable("id") UUID id) {
        orderService.deleteById(id);
       return null;
   }

}
