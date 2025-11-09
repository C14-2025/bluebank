package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.AccountRequestDTO;
import com.inatel.blue_bank.model.dto.AccountResponseDTO;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.repository.CustomerRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public abstract class AccountMapper {

    @Autowired
    CustomerRepository customerRepository;

    @Mapping(target = "customer", expression = "java( customerRepository.findById(dto.customerId()).orElse(null))")
    public abstract Account toEntity(AccountRequestDTO dto);

    public abstract AccountResponseDTO toResponseDTO(Account account);
}
