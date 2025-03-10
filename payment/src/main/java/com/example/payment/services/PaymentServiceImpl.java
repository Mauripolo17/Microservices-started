package com.example.payment.services;

import com.example.payment.entities.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> updatePayment(Payment payment, UUID id) {
        return paymentRepository.findById(id).map(payment1 -> {
            payment1.setAmount(payment.getAmount());
            payment1.setDescription(payment.getDescription());
            payment1.setDate(payment.getDate());
            return paymentRepository.save(payment);
        });

    }

    @Override
    public void deletePayment(UUID id) {
         paymentRepository.deleteById(id);
    }
}
