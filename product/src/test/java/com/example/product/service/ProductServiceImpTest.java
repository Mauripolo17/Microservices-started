//package com.example.product.service;
//
//import com.example.product.entity.Product;
//import com.example.product.repository.ProductRepository;
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.testcontainers.shaded.org.checkerframework.checker.units.qual.Speed;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//class ProductServiceImpTest {
//
//    private ProductServiceImp productServiceImp;
//    private Product product;
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    void setup(){
//        this.productRepository= Mockito.mock(ProductRepository.class);
//        this.productServiceImp = new ProductServiceImp(productRepository);
//
//        product = new Product();
//        product.setId(UUID.randomUUID());
//        product.setName("Producto de prueba");
//        product.setDescription("Descripción del producto");
//        product.setPrice(100.0);
//
//    }
//    @Test
//    void getProductById() {
//        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
//        Optional<Product> foundProduct = productServiceImp.getProductById(product.getId());
//        assertTrue(foundProduct.isPresent());
//        assertEquals("Producto de prueba", foundProduct.get().getName());
//    }
//
//    @Test
//    void getAllProducts() {
//        when(productRepository.findAll()).thenReturn(List.of(product));
//        List<Product> foundProducts = productServiceImp.getAllProducts();
//        assertNotNull(foundProducts);
//        assertFalse(foundProducts.isEmpty());
//        assertEquals(product.getId(), foundProducts.get(0).getId());
//    }
//
//    @Test
//    void deleteProduct() {
//        UUID uuid = UUID.randomUUID();
//        productServiceImp.deleteProduct(uuid);
//        Mockito.verify(productRepository, Mockito.times(1)).deleteById(uuid);
//    }
//
//    @Test
//    void saveProduct() {
//        Product product1 = new Product();
//        product1.setId(UUID.randomUUID());
//        product1.setName("Producto de prueba numero 2");
//        product1.setDescription("Descripción del producto numero 2");
//        product1.setPrice(1000.0);
//
//        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product1);
//
//        Product createdProduct = productServiceImp.saveProduct(product1);
//        assertNotNull(createdProduct);
//        assertEquals(product1.getId(), createdProduct.getId());
//        assertEquals(product1.getName(), createdProduct.getName());
//        assertEquals(product1.getDescription(), createdProduct.getDescription());
//        assertEquals(product1.getPrice(), createdProduct.getPrice());
//    }
//
//    @Test
//    void updateProduct() {
//        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
//        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
//        product.setName("Producto de prueba actualizado");
//        Optional<Product> updatedProduct = productServiceImp.updateProduct(product.getId(), product);
//        assertTrue(updatedProduct.isPresent());
//        assertEquals("Producto de prueba actualizado", updatedProduct.get().getName());
//    }
//}