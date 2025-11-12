package com.inatel.blue_bank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        BigDecimal amount,
        AccountResponseDTO payer,
        AccountResponseDTO payee,
        LocalDateTime createdAt
) {}
