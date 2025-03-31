package com.example.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventoryFallback")
public class InventoryFallbackController {

    @GetMapping
    public Mono<String> inventoryFallback() {
        return Mono.just("Inventory service is currently unavailable. Please try again later.");
    }
}
