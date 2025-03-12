package com.example.payment.controllers;

import com.example.payment.entities.Payment;
import com.example.payment.repository.PaymentRepository;
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
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer  = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentRepository paymentRepository;

    private UUID paymentId;

    @BeforeEach
    void setup() {
        paymentRepository.deleteAll();

        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setDescription("Pago de prueba");
        payment.setDate(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        paymentId = payment.getId();
    }

    @Test
    void testGetAllPayments() {
        webTestClient.get()
                .uri("/api/payment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Payment.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<Payment> payments = response.getResponseBody();
                    assertNotNull(payments);
                    assertEquals("Pago de prueba", payments.get(0).getDescription());
                });
    }

    @Test
    void testGetPaymentById() {
        webTestClient.get()
                .uri("/api/payment/{id}", paymentId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .consumeWith(response -> {
                    Payment payment = response.getResponseBody();
                    assertNotNull(payment);
                    assertEquals("Pago de prueba", payment.getDescription());
                });
    }

    @Test
    void testCreatePayment() {
        Payment newPayment = new Payment();
        newPayment.setAmount(BigDecimal.valueOf(250.0));
        newPayment.setDescription("Nuevo pago");
        newPayment.setDate(LocalDateTime.now());

        webTestClient.post()
                .uri("/api/payment")
                .bodyValue(newPayment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .consumeWith(response -> {
                    Payment payment = response.getResponseBody();
                    assertNotNull(payment);
                    assertEquals("Nuevo pago", payment.getDescription());
                });
    }

    @Test
    void testUpdatePayment() {
        Payment updatedPayment = new Payment();
        updatedPayment.setAmount(BigDecimal.valueOf(500.0));
        updatedPayment.setDescription("Pago actualizado");
        updatedPayment.setDate(LocalDateTime.now());

        webTestClient.put()
                .uri("/api/payment/{id}", paymentId)
                .bodyValue(updatedPayment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .consumeWith(response -> {
                    Payment payment = response.getResponseBody();
                    assertNotNull(payment);
                    assertEquals("Pago actualizado", payment.getDescription());
                    assertEquals(BigDecimal.valueOf(500.0), payment.getAmount());
                });
    }

    @Test
    void testDeletePayment() {
        webTestClient.delete()
                .uri("/api/payment/{id}", paymentId)
                .exchange()
                .expectStatus().isOk();

        assertFalse(paymentRepository.findById(paymentId).isPresent());
    }
}