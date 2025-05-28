package com.example.order.WebClient;

import com.example.order.WebClient.DTO.Payment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PaymentClient {

    private final WebClient webClient;

    public PaymentClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://payment/api/payment")
                .build();
    }

    public Mono<Payment> savePayment(Payment payment) {
        return webClient.post().uri("")
                .bodyValue(payment)
                .retrieve()
                .bodyToMono(Payment.class);
    }
}
