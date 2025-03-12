package com.example.payment.controllers;

import com.example.payment.entities.Payment;
import com.example.payment.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private PaymentService paymentService;
    private Payment payment;
//    @Autowired
//    private PaymentRepository paymentRepository;

    @BeforeEach
    void setup() {
        payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(30000));
        payment.setDate(LocalDateTime.now());
        payment.setDescription("Test Payment");
        payment.setTransactionId(UUID.randomUUID().toString());

    }

    @Test
    void getPayments() {
        Mockito.when(paymentService.getAllPayments()).thenReturn(List.of(payment));
        webTestClient.get().uri("/api/payment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Payment.class).hasSize(1).contains(payment);
    }

    @Test
    void getPayment() {
        Mockito.when(paymentService.getPaymentById(payment.getId())).thenReturn(Optional.of(payment));
        webTestClient.get().uri("/api/payment/{id}", payment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void savePayment() {
        Mockito.when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);
        webTestClient.post().uri("/api/payment")
                .bodyValue(payment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void updatePayment() {
        Mockito.when(paymentService.getPaymentById(payment.getId())).thenReturn(Optional.of(payment));
        Mockito.when(paymentService.updatePayment(any(Payment.class), any(UUID.class) )).thenReturn(Optional.of(payment));
        webTestClient.put().uri("/api/payment/{id}", payment.getId(), payment )
                .bodyValue(payment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void deletePayment() {
        webTestClient.delete().uri("/api/payment/{id}", payment.getId())
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(paymentService, Mockito.times(1)).deletePayment(payment.getId());
    }
}