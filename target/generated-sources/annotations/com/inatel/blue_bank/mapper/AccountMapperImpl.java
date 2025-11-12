package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.AccountRequestSaveDTO;
import com.inatel.blue_bank.model.dto.AccountResponseDTO;
import com.inatel.blue_bank.model.dto.CustomerResponseDTO;
import com.inatel.blue_bank.model.entity.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T02:33:34-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl extends AccountMapper {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Account toEntity(AccountRequestSaveDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Account account = new Account();

        account.setAccountNumber( dto.accountNumber() );
        account.setBalance( dto.balance() );
        account.setBranchCode( dto.branchCode() );

        account.setCustomer( customerService.findById(dto.customerId()).orElse(null) );

        return account;
    }

    @Override
    public AccountResponseDTO toResponseDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        UUID id = null;
        Long accountNumber = null;
        BigDecimal balance = null;
        Integer branchCode = null;
        CustomerResponseDTO customer = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = account.getId();
        if ( account.getAccountNumber() != null ) {
            accountNumber = Long.parseLong( account.getAccountNumber() );
        }
        balance = account.getBalance();
        branchCode = account.getBranchCode();
        customer = customerMapper.toResponseDTO( account.getCustomer() );
        createdAt = account.getCreatedAt();
        updatedAt = account.getUpdatedAt();

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO( id, accountNumber, balance, branchCode, customer, createdAt, updatedAt );

        return accountResponseDTO;
    }
}
