package com.example.product.service;

import com.example.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    Optional<Product> getProductById(UUID id);
    List<Product> getAllProducts();
    void deleteProduct(UUID id);
    Product saveProduct(Product product);
    Optional<Product> updateProduct(UUID id, Product product);

}
