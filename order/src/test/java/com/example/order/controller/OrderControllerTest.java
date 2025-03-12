package com.example.order.controller;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OrderRepository orderRepository;

    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        Order orderTest = new Order();
        orderTest.setDate(LocalDate.parse("2023-09-12"));
        orderTest.setProducts(Collections.singletonList(UUID.randomUUID()));
        orderTest.setPayment(UUID.randomUUID());
        orderTest = orderRepository.save(orderTest);


        orderId = orderTest.getId();

    }

    @Test
    void getOrderByID() {
        webTestClient.get().uri("/api/orders/{id}", orderId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .consumeWith(response -> {
                    Order order = response.getResponseBody();
                    assertNotNull(order);
                    assertEquals("Order date: ", order.getDate());
                });

    }

    @Test
    void getAllOrders() {
        webTestClient.get().uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<Order> order = response.getResponseBody();
                    assertNotNull(order);
                    assertEquals("Order date: ", order.get(0).getDate());
                });
    }

    @Test
    void createOrder() {
        Order newOrder = new Order();
        newOrder.setDate(LocalDate.parse("2023-09-12"));
        newOrder.setProducts(Collections.singletonList(UUID.randomUUID()));
        newOrder.setPayment(UUID.randomUUID());

        webTestClient.post()
                .uri("/api/orders")
                .bodyValue(newOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .consumeWith(response -> {
                            Order order = response.getResponseBody();
                            assertNotNull(order);
                            assertEquals("Order date: ", order.getDate());
                        }
                );

    }

    @Test
    void updateOrder() {
        Order updateOrder = new Order();
        updateOrder.setDate(LocalDate.parse("2023-09-12"));
        updateOrder.setProducts(Collections.singletonList(UUID.randomUUID()));
        updateOrder.setPayment(UUID.randomUUID());

        webTestClient.put().uri("/api/orders/{id}", orderId)
                .bodyValue(updateOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .consumeWith(response -> {
                    Order order = response.getResponseBody();
                    assertNotNull(order);
                    assertEquals("Updated Order date: ", order.getDate());
                });
    }

    @Test
    void deleteOrder() {
        webTestClient.delete().uri("/api/orders/{id}", orderId)
                .exchange()
                .expectStatus().isOk();

        assertFalse(orderRepository.findById(orderId).isPresent());
    }
}