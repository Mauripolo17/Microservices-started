package com.example.payment.services;

import com.example.payment.entities.Payment;


import java.util.Optional;
import java.util.UUID;

public interface PaymentService {

    Optional<Payment> getPaymentById(UUID id);
    Payment createPayment(Payment payment);
    Optional<Payment> updatePayment(Payment payment, UUID id);
    void deletePayment(UUID id);
}
