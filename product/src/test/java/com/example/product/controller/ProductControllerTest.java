package com.example.product.controller;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer  = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    private UUID productId;
    private Product product;
    @BeforeEach
    void setup() {
        productRepository.deleteAll();

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test");
        product.setDescription("Test");
        product.setPrice(1000.0);
        productRepository.save(product);

        productId = product.getId();
    }



    @Test
    void testGetProductById() {
        webTestClient.get()
                .uri("/api/product/{id}", productId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product = response.getResponseBody();
                    assertNotNull(product);
                    assertEquals("Test", product.getDescription());
                });
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName("Test2");
        newProduct.setDescription("Test2");
        newProduct.setPrice(2000.0);

        webTestClient.post()
                .uri("/api/product")
                .bodyValue(newProduct)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product = response.getResponseBody();
                    assertNotNull(product);
                    assertEquals("Test2", product.getDescription());
                });
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Test2 update");
        updatedProduct.setDescription("Test2 update");
        updatedProduct.setPrice(2000.0);

        webTestClient.put()
                .uri("/api/product/{id}", productId)
                .bodyValue(updatedProduct)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product = response.getResponseBody();
                    assertNotNull(product);
                    assertEquals("Test2 update", product.getDescription());
                });
    }

    @Test
    void testDeleteProduct() {
        webTestClient.delete()
                .uri("/api/product/{id}", productId)
                .exchange()
                .expectStatus().isOk();

        assertFalse(productRepository.findById(productId).isPresent());
    }
}