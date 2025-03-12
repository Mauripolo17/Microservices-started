package com.example.payment.controllers;

import com.example.payment.entities.Payment;
import com.example.payment.services.PaymentService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private  final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    public Flux<Payment> getPayments() {
        return Flux.fromIterable(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public Mono<Payment> getPayment(@PathVariable("id") UUID id){
        return Mono.justOrEmpty(paymentService.getPaymentById(id));
    }

    @PostMapping()
    public Mono<Payment> savePayment(@RequestBody Payment payment){
        return Mono.justOrEmpty(paymentService.createPayment(payment));
    }

    @PutMapping("/{id}")
    public Mono<Payment> updatePayment(@PathVariable("id") UUID id, @RequestBody Payment payment){
        return Mono.justOrEmpty(paymentService.updatePayment(payment, id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePayment(@PathVariable UUID id) {
        return Mono.fromRunnable(() -> paymentService.deletePayment(id));
    }


}
