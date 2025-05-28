package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImp(ProductRepository productRepository ) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    @Cacheable(value = "products", key = "'allProducts'")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        product.setId(UUID.randomUUID());
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(UUID id, Product product) {
        return productRepository.findById(id).map(
                productInBD ->{
                    productInBD.setName(product.getName());
                    productInBD.setDescription(product.getDescription());
                    productInBD.setPrice(product.getPrice());
                    return productRepository.save(productInBD);
                }
        );
    }

    @Override
    public List<Product> getProductsByIds(List<UUID> ids) {
        return productRepository.findAllById(ids);
    }
}
