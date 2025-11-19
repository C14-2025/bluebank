package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.TransactionRequestDTO;
import com.inatel.blue_bank.model.dto.TransactionResponseDTO;
import com.inatel.blue_bank.model.entity.Transaction;
import com.inatel.blue_bank.service.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public abstract class TransactionMapper {

    @Autowired
    AccountService accountService;

    @Mapping(target = "payer", expression = "java( accountService.findByCustomerEmail(dto.payerEmail()).orElse(null))")
    @Mapping(target = "payee", expression = "java( accountService.findByCustomerEmail(dto.payeeEmail()).orElse(null))")
    public abstract Transaction toEntity(TransactionRequestDTO dto);

    public abstract TransactionResponseDTO toResponseDTO(Transaction transaction);
}
