package com.example.order.WebClient.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Payment(
        UUID id,
        String description,
        LocalDateTime date,
        BigDecimal amount,
        String transactionId
) {
}
