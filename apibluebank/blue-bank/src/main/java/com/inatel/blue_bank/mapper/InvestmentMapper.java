package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.investment.InvestmentResponseDTO;
import com.inatel.blue_bank.model.dto.investment.InvestmentSaveDTO;
import com.inatel.blue_bank.model.entity.Investment;
import com.inatel.blue_bank.service.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public abstract class InvestmentMapper {

    @Autowired
    AccountService accountService;

    @Mapping(target = "account", expression = "java( accountService.findById(dto.accountId()).orElse(null))")
    public abstract Investment toEntity(InvestmentSaveDTO dto);

    public abstract InvestmentResponseDTO toResponseDTO(Investment investment);
}
