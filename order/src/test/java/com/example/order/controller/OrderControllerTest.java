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
import java.util.Optional;
import java.util.UUID;

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

    @MockitoBean
    private OrderService orderService;
    private Order orderTest;


    @BeforeEach
    void setUp() {
        orderTest = new Order();
        orderTest.setId(UUID.randomUUID());
        orderTest.setDate(LocalDate.parse("2023-09-12"));
        orderTest.setProducts(Collections.singletonList(UUID.randomUUID()));
        orderTest.setPayment(UUID.randomUUID());

    }

    @Test
    void getOrderByID() {
        when(orderService.findById(orderTest.getId())).thenReturn(Optional.of(orderTest));
        webTestClient.get().uri("/api/orders/{id}", orderTest.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(orderTest.getId().toString())
                .jsonPath("$.date").isEqualTo(orderTest.getDate())
                .jsonPath("$.products").isNotEmpty()
                .jsonPath("$.payment").isEqualTo(orderTest.getPayment().toString());

    }

    @Test
    void getAllOrders() {
        when(orderService.findAll()).thenReturn(Collections.singletonList(orderTest));
        webTestClient.get().uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].date").isEqualTo("2023-09-12")
                .jsonPath("$[0].products").isNotEmpty()
                .jsonPath("$[0].payment").isEqualTo(orderTest.getPayment().toString());
    }

    @Test
    void createOrder() {
        when(orderService.save(any(Order.class))).thenReturn(orderTest);
        webTestClient.post()
                .uri("/api/orders")
                .body(Mono.just(orderTest), Order.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.date").isEqualTo("2023-09-12")
                .jsonPath("$.products").isNotEmpty()
                .jsonPath("$.payment").isNotEmpty();

    }

    @Test
    void updateOrder() {
        when(orderService.findById(orderTest.getId())).thenReturn(Optional.of(orderTest));
        when(orderService.updateOrder(any(UUID.class), any(Order.class))).thenReturn(Optional.of(orderTest));
        webTestClient.put().uri("/api/orders/{id}", orderTest.getId(), orderTest)
                .bodyValue(orderTest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(orderTest.getId())
                .jsonPath("$.date").isEqualTo(orderTest.getDate())
                .jsonPath("$.products").isNotEmpty()
                .jsonPath("$.payment").isEqualTo(orderTest.getPayment());
    }

    @Test
    void deleteOrder() {
        webTestClient.delete().uri("/api/orders/{id}", orderTest.getId())
                .exchange()
                .expectStatus().isOk();

        verify(orderService, times(1)).deleteById(orderTest.getId());
    }
}