package com.example.inventory.controllers;

import com.example.inventory.entities.Inventory;
import com.example.inventory.repositories.InventoryRepository;
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
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory savedInventory;

    @BeforeEach
    // Arrange
    void setup() {
        inventoryRepository.deleteAll();

        Inventory inventory = new Inventory();
        inventory.setProductId(UUID.randomUUID());
        inventory.setQuantity(23);
        savedInventory = inventoryRepository.save(inventory);
    }

    @Test
    public void testGetAllInventories() {
        // Act & Assert
        webTestClient.get()
                .uri("/api/inventories")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].quantity").isEqualTo(savedInventory.getQuantity());
    }

    @Test
    public void testGetInventoryById() {
        // Act & Assert
        webTestClient.get()
                .uri("/api/inventories/{id}", savedInventory.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedInventory.getId().toString())
                .jsonPath("$.quantity").isEqualTo(savedInventory.getQuantity());
    }

    @Test
    public void testGetInventoryByProductId() {
        // Act & Assert
        webTestClient.get()
                .uri("/api/inventories/product/{productId}", savedInventory.getProductId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedInventory.getId().toString())
                .jsonPath("$.productId").isEqualTo(savedInventory.getProductId().toString());
    }

    @Test
    public void testCreateInventory() {
        // Arrange
        Inventory newInventory = new Inventory();
        newInventory.setProductId(UUID.randomUUID());
        newInventory.setQuantity(5);

        // Act & Assert
        webTestClient.post()
                .uri("/api/inventories")
                .body(Mono.just(newInventory), Inventory.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.quantity").isEqualTo(5);
    }

    @Test
    public void testUpdateInventory() {
        // Arrange
        savedInventory.setQuantity(50);

        // Act & Assert
        webTestClient.put()
                .uri("/api/inventories/{id}", savedInventory.getId())
                .body(Mono.just(savedInventory), Inventory.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedInventory.getId().toString())
                .jsonPath("$.quantity").isEqualTo(50);
    }

    @Test
    public void testDeleteInventory() {
        // Act & Assert
        webTestClient.delete()
                .uri("/api/inventories/{id}", savedInventory.getId())
                .exchange()
                .expectStatus().isOk();

        assertThat(inventoryRepository.findById(savedInventory.getId())).isEmpty();
    }
}