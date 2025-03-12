package com.example.inventory.services;

import com.example.inventory.entities.Inventory;
import com.example.inventory.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryServiceImp implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImp(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(UUID id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Optional<Inventory> findByProductId(UUID id) {
        return inventoryRepository.findByProductId(id);
    }

    @Override
    public Optional<Inventory> update(UUID id, Inventory inventory) {
        return inventoryRepository.findById(id).map(
                inventoryInBD-> {inventoryInBD.setQuantity(inventory.getQuantity());
                    return  inventoryRepository.save(inventoryInBD);
                }
        );
    }

    @Override
    public void deleteById(UUID id) {
        inventoryRepository.deleteById(id);
    }
}
