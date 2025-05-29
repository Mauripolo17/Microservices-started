package com.example.gateway.controllers;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final WebClient webClient;

    public AuthController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://keycloak:9191/realms/Microservices-started/protocol/openid-connect").build();
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> credentials) {
        // credentials deben tener "username" y "password"

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", "Mi-app"); // reemplaza con tu client_id
        formData.add("client_secret", "999SoGQBdCXpsHbKkIKNhBw3JUfMXrFy"); // si aplica
        formData.add("username", credentials.get("username"));
        formData.add("password", credentials.get("password"));

        return webClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
    }
}
