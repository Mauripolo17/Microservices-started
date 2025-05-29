package com.example.gateway.controllers;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/serviceFallback")
public class ServiceFallbackController {

    private final RedisTemplate<String, Object> redisTemplate;

    public ServiceFallbackController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{service}")
    public Mono<String> serviceFallback(@PathVariable String service) {
        return Mono.just(String.format("%s service is currently unavailable. Please try again later.", service));
    }

    @GetMapping("/Product")
    public ResponseEntity<Object> getCachedProducts() {
        Object products = redisTemplate.opsForValue().get("products::allProducts");

        if (products != null) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.status(503).body("Product service is currently unavailable. Please try again later");
        }
    }

    @GetMapping("/Payment")
    public ResponseEntity<Object> getCachedPayments() {
        Object payments = redisTemplate.opsForValue().get("payments::allPayments");
        System.out.println(payments);
        if (payments != null) {
            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.status(503).body("Payment service is currently unavailable. Please try again later.");
        }
    }
}
