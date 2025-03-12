package com.example.payment.services;

import com.example.payment.entities.Payment;
import com.example.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    private PaymentServiceImpl paymentService;
    private Payment payment;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        this.paymentRepository = Mockito.mock(PaymentRepository.class);
        this.paymentService = new PaymentServiceImpl(paymentRepository);

        payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setDate(LocalDateTime.now());
        payment.setDescription("Test");
        payment.setAmount(BigDecimal.valueOf(30000));
        payment.setTransactionId(UUID.randomUUID().toString());

    }

    @Test
    void getPaymentById() {
        Mockito.when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        Payment foundPayment = paymentService.getPaymentById(payment.getId()).orElse(null);
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
        assertEquals(payment.getDescription(), foundPayment.getDescription());
        assertEquals(payment.getAmount(), foundPayment.getAmount());

    }

    @Test
    void getAllPayments() {
        Mockito.when(paymentRepository.findAll()).thenReturn(List.of(payment));
        List<Payment> foundPayments = paymentService.getAllPayments();
        assertNotNull(foundPayments);
        assertFalse(foundPayments.isEmpty());
        assertEquals(payment.getId(), foundPayments.get(0).getId());
    }

    @Test
    void createPayment() {

        Payment payment1 = new Payment();
        payment1.setId(UUID.randomUUID());
        payment1.setDescription("Test2");
        payment1.setDate(LocalDateTime.now());
        payment1.setAmount(BigDecimal.valueOf(50000));
        payment1.setTransactionId(UUID.randomUUID().toString());

        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment1);


        Payment createdPayment = paymentService.createPayment(payment1);

        assertNotNull(createdPayment);
        assertEquals(payment1.getId(), createdPayment.getId());
        assertEquals(payment1.getDescription(), createdPayment.getDescription());
        assertEquals(payment1.getAmount(), createdPayment.getAmount());
        assertEquals(payment1.getTransactionId(), createdPayment.getTransactionId());

    }

    @Test
    void updatePayment() {
        Mockito.when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        payment.setDescription("Descripcion actualizada");
        Payment updatedPayment = paymentService.updatePayment(payment, payment.getId()).orElse(null);
        assertNotNull(updatedPayment);
        assertEquals("Descripcion actualizada", updatedPayment.getDescription());

    }

    @Test
    void deletePayment() {
        UUID uuid = UUID.randomUUID();
        paymentService.deletePayment(uuid);
        Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(uuid);
    }
}