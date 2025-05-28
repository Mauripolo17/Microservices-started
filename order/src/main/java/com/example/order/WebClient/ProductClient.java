package com.example.order.WebClient;


import com.example.order.WebClient.DTO.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://product/api/product")
                .build();
    }

    public Flux<Product> getAllProductsByIds(List<UUID> productsIds){
        return webClient.post()
                .uri("/all-products")
                .bodyValue(productsIds).retrieve()
                .bodyToFlux(Product.class);
    }

}
