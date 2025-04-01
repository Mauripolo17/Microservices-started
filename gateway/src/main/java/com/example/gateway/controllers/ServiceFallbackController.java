package com.example.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/serviceFallback")
public class ServiceFallbackController {

    @GetMapping("/{service}")
    public Mono<String> serviceFallback(@PathVariable String service) {
        return Mono.just(String.format("%s service is currently unavailable. Please try again later.", service));
    }
}
