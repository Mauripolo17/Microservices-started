package com.example.order.WebClient.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record Inventory(
        UUID id,
        UUID productId,
        int quantity
) {
}
