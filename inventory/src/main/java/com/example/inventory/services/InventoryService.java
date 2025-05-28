package com.example.inventory.services;

import com.example.inventory.entities.Inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryService {

    Inventory save(Inventory inventory);
    List<Inventory> findAll();
    Optional<Inventory> findById(UUID id);
    Optional<Inventory> findByProductId(UUID id);
    Optional<Inventory> update(UUID id, Inventory inventory);
    void deleteById(UUID id);
    Boolean areInventoriesAvailable(List<UUID> productIds);
    List<Inventory> updateInventoryAvailability(List<UUID> productIds);
}