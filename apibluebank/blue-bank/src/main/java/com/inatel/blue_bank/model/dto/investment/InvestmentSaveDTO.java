package com.inatel.blue_bank.model.dto.investment;

import java.math.BigDecimal;
import java.util.UUID;

public record InvestmentSaveDTO(
        String ticker,
        String name,
        Double share,
        BigDecimal costPerShare,
        UUID accountId
) {}
