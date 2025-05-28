package com.example.product.repository;

import com.example.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, UUID> {
    List<Product> findByIdIn(List<UUID> ids);
}
