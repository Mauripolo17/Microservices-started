package com.example.order.WebClient;

import com.example.order.WebClient.DTO.Inventory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryClient {

    private final WebClient webClient;


    public InventoryClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://inventory/api/inventory")
                .build();
    }

    public Mono<Boolean> checkInventory(List<UUID> productIds) {
        return webClient.post()
                .uri("/check-inventories")
                .bodyValue(productIds)
                .retrieve()
                .bodyToMono(Boolean.class);

    }

    public Flux<Inventory> updateInventories(List<UUID> productIds) {
        return webClient.put()
                .uri("/update-inventories")
                .bodyValue(productIds)
                .retrieve()
                .bodyToFlux(Inventory.class);
    }
}
