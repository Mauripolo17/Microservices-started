package com.example.gateway.config;

import com.example.gateway.filters.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, LoggingFilter loggingFilter) {
        return builder.routes()
                .route("product-service", r -> r.path("/api/product/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("productCircuitBreaker")
                                        .setFallbackUri("forward:/serviceFallback/Product"))
                        )
                        .uri("lb://PRODUCT"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f
                                .filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/serviceFallback/Order"))
                        )
                        .uri("lb://ORDER"))
                .route("payment-service", r -> r.path("/api/payment/**")
                        .filters(f -> f
                                .filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("paymentCircuitBreaker")
                                        .setFallbackUri("forward:/serviceFallback/Payment"))
                        )
                        .uri("lb://PAYMENT"))
                .route("inventory-service", r -> r.path("/api/inventories/**")
                        .filters(f -> f
                                .filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("inventoryCircuitBreaker")
                                        .setFallbackUri("forward:/serviceFallback/Inventory"))
                                )
                        .uri("lb://INVENTORY"))
                .build();
    }
}
