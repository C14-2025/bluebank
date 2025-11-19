package com.inatel.blue_bank.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDTO(

        @NotNull(message = "Mandatory field")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Mandatory field")
        String payerEmail,

        @NotNull(message = "Mandatory field")
        String payeeEmail
) {}