package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    private final RedisTemplate<String, Object> redisTemplate;


    public ProductServiceImp(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    @Cacheable(value = "getAllProducts")
    public List<Product> getAllProducts() {

        List<Product> cachedProducts = (List<Product>) redisTemplate.opsForValue().get("getAllProducts");
        if (cachedProducts != null) {
            return cachedProducts;
        }
        List<Product> products = productRepository.findAll();
        redisTemplate.opsForValue().set("getAllProducts", products, 10, TimeUnit.MINUTES);
        return products;
    }
    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product saveProduct(Product product) {
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
}
