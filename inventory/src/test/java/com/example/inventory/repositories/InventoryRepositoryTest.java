package com.example.inventory.repositories;

import com.example.inventory.entities.Inventory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
class InventoryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    public void testFindByProductId() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(30);
        inventoryRepository.save(inventory);

        // Act
        Optional<Inventory> foundInventory = inventoryRepository.findByProductId(productId);

        // Assert
        if (foundInventory.isPresent()) {
            inventory = foundInventory.get();
            assertThat(inventory.getQuantity()).isEqualTo(30);
        } else {
            fail("Inventory not found");
        }
    }
}