package com.inatel.blue_bank.model.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountRequestSaveDTO(

        @NotNull(message = "Account number is mandatory")
        @Pattern(regexp = "\\d{8,12}", message = "Account number must have between 8 and 12 digits")
        String accountNumber,

        @NotNull(message = "Initial balance is mandatory")
        @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be greater than or equal to 0")
        BigDecimal balance,

        @NotNull(message = "Branch code is mandatory")
        @Min(value = 1, message = "Branch code must be between 1 and 999")
        @Max(value = 999, message = "Branch code must be between 1 and 999")
        Integer branchCode,

        @NotNull(message = "Customer ID is mandatory")
        UUID customerId
) {}