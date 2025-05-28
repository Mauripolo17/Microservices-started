package com.example.order.WebClient.DTO;

import java.util.UUID;

public record Product(
        UUID id,
        String name,
        String description,
        Double price
) {
}
