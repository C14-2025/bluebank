package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.account.AccountRequestSaveDTO;
import com.inatel.blue_bank.model.dto.account.AccountResponseDTO;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.service.CustomerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public abstract class AccountMapper {

    @Autowired
    CustomerService customerService;

    @Mapping(target = "customer", expression = "java( customerService.findById(dto.customerId()).orElse(null))")
    public abstract Account toEntity(AccountRequestSaveDTO dto);

    public abstract AccountResponseDTO toResponseDTO(Account account);
}
