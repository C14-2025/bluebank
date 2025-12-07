package com.inatel.blue_bank.model.dto.account;

import com.inatel.blue_bank.model.dto.customer.CustomerResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        Long accountNumber,
        BigDecimal balance,
        Integer branchCode,
        CustomerResponseDTO customer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}