package com.inatel.blue_bank.model.dto.investment;

import java.math.BigDecimal;

public record InvestmentUpdateDTO(
        String name,
        Double share,
        BigDecimal costPerShare
) {}
