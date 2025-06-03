package com.example.inventory.services;

import com.example.inventory.dtos.BaseResponse;
import com.example.inventory.entities.Inventory;
import com.example.inventory.repositories.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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



    @Override
    public Boolean isInStock(UUID productId) {
        var inventory = inventoryRepository.findByProductId(productId);
        return inventory.isPresent();
    }

    @Override
    @Transactional
    public BaseResponse areInStock(List<UUID> productIds) {
        var errorList = new ArrayList<String>();

        // Get inventories by productIds
        List<Inventory> inventoryList = inventoryRepository.findByProductIdIn(productIds);

        // How many times a product repeats (for quantity in order)
        Map<UUID, Long> productCountMap = productIds.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // Verify disponibility
        for (Map.Entry<UUID, Long> entry : productCountMap.entrySet()) {
            UUID productId = entry.getKey();
            long count = entry.getValue();

            var inventoryOpt = inventoryList.stream()
                    .filter(inv -> inv.getProductId().equals(productId))
                    .findFirst();

            if (inventoryOpt.isEmpty()) {
                errorList.add("Product " + productId + " not found");
            } else if (inventoryOpt.get().getQuantity() < count) {
                errorList.add("Product " + productId + " is out of stock");
            }
        }

        // If there are errors, return without modifying the inventory
        if (!errorList.isEmpty()) {
            return new BaseResponse(errorList.toArray(new String[0]));
        }

        // If everything is ok, update the quantity of products
        for (Map.Entry<UUID, Long> entry : productCountMap.entrySet()) {
            UUID productId = entry.getKey();
            long count = entry.getValue();

            Inventory inventory = inventoryList.stream()
                    .filter(inv -> inv.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow();

            inventory.setQuantity(inventory.getQuantity() - (int) count);
        }

        inventoryRepository.saveAll(inventoryList); // save changes

        return new BaseResponse(new String[0]); // return a new BaseResponse without errors
    }

}
