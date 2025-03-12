package com.example.payment.repository;


import com.example.payment.entities.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest
class PaymentRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer  = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    PaymentRepository paymentRepository;
    @Test
    public void testFindByPaymentId() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setDescription("Test");
        payment.setDate(LocalDateTime.now());

        // Act
        Optional<Payment> foundPayment = paymentRepository.findById(paymentId);

        // Assert
        if (foundPayment.isPresent()) {
            payment = foundPayment.get();
            assertThat(payment.getDescription()).isEqualTo("Test");
        } else {
            fail("Payment not found");
        }
    }
}