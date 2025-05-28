package com.example.inventory.controllers;

import com.example.inventory.entities.Inventory;
import com.example.inventory.services.InventoryService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Flux<Inventory> getAllInventories() {
        return Flux.fromIterable(inventoryService.findAll());
    }

    @GetMapping("/{id}")
    public Mono<Inventory> getInventoryById(@PathVariable("id")  UUID id) {
        return Mono.justOrEmpty(inventoryService.findById(id));
    }

    @GetMapping("/product/{productId}")
    public Mono<Inventory> getInventoryByProductId(@PathVariable("productId")  UUID productId) {
        return Mono.justOrEmpty(inventoryService.findByProductId(productId));
    }

    @PostMapping
    public Mono<Inventory> createInventory(@RequestBody Inventory inventory) {
        return Mono.justOrEmpty(inventoryService.save(inventory));
    }

    @PutMapping("/{id}")
    public Mono<Inventory> updateInventory(@PathVariable("id")  UUID id, @RequestBody Inventory inventory) {
        return Mono.justOrEmpty(inventoryService.update(id, inventory));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteInventory(@PathVariable("id")  UUID id) {
        inventoryService.deleteById(id);
        return null;
    }

    @PostMapping("/check-inventories")
    public Mono<Boolean> checkInventory(@RequestBody List<UUID> productIds) {
        return Mono.justOrEmpty(inventoryService.areInventoriesAvailable(productIds));
    }

    @PutMapping("/update-inventories")
    public Flux<Inventory> updateInventories(@RequestBody List<UUID> productIds) {
        return Flux.fromIterable(inventoryService.updateInventoryAvailability(productIds));
    }


}
