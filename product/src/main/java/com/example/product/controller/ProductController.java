package com.example.product.controller;

import com.example.product.entity.Product;
import com.example.product.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable("id") UUID id) {
        return Mono.justOrEmpty(productService.getProductById(id));
    }

    @GetMapping("/All")
    @PreAuthorize("hasRole('user')")
    public Flux<Product> getAllProducts() {
        return Flux.fromIterable(productService.getAllProducts());
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return Mono.just(productService.saveProduct(product));
    }


    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable("id") UUID id, @RequestBody Product product) {
        return Mono.justOrEmpty(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProduct(id);
        return Mono.empty();
    }
}
