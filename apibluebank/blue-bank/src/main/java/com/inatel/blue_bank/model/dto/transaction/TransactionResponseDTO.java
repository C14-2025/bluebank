package com.inatel.blue_bank.model.dto.transaction;

import com.inatel.blue_bank.model.dto.account.AccountResponseDTO;

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
