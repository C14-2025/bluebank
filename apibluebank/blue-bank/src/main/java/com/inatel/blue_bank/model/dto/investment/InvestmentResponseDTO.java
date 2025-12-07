package com.inatel.blue_bank.model.dto.investment;

import com.inatel.blue_bank.model.dto.account.AccountResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvestmentResponseDTO(
        UUID id,
        String ticker,
        String name,
        Double share,
        BigDecimal costPerShare,
        AccountResponseDTO account,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
