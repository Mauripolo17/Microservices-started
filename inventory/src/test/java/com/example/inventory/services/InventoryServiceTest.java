package com.example.inventory.services;

import com.example.inventory.entities.Inventory;
import com.example.inventory.repositories.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImp inventoryService;

    @Test
    public void testFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setProductId(UUID.randomUUID());
        inventory.setQuantity(23);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));

        // Act
        Optional<Inventory> foundInventory = inventoryService.findById(id);

        // Assert
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getQuantity()).isEqualTo(23);
    }

    @Test
    public void testSaveInventory() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setProductId(UUID.randomUUID());
        inventory.setQuantity(23);

        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // Act
        Inventory savedInventory = inventoryService.save(inventory);

        // Assert
        assertThat(savedInventory.getQuantity()).isEqualTo(23);
    }

    @Test
    public void testUpdateInventory() {
        // Arrange
        UUID id = UUID.randomUUID();
        Inventory existingInventory = new Inventory();
        existingInventory.setId(id);
        existingInventory.setProductId(UUID.randomUUID());
        existingInventory.setQuantity(5);

        Inventory updatedInventory = new Inventory();
        updatedInventory.setQuantity(15);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(existingInventory);

        // Act
        Optional<Inventory> result = inventoryService.update(id, updatedInventory);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getQuantity()).isEqualTo(15);
    }

    @Test
    public void testDeleteInventory() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        inventoryService.deleteById(id);

        // Assert
        Mockito.verify(inventoryRepository, times(1)).deleteById(id);
    }
}