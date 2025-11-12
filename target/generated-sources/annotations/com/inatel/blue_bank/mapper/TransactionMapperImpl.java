package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.AccountResponseDTO;
import com.inatel.blue_bank.model.dto.TransactionRequestDTO;
import com.inatel.blue_bank.model.dto.TransactionResponseDTO;
import com.inatel.blue_bank.model.entity.Transaction;
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
public class TransactionMapperImpl extends TransactionMapper {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Transaction toEntity(TransactionRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setAmount( dto.amount() );

        transaction.setPayer( accountService.findByCustomerEmail(dto.payerEmail()).orElse(null) );
        transaction.setPayee( accountService.findByCustomerEmail(dto.payeeEmail()).orElse(null) );

        return transaction;
    }

    @Override
    public TransactionResponseDTO toResponseDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        UUID id = null;
        BigDecimal amount = null;
        AccountResponseDTO payer = null;
        AccountResponseDTO payee = null;
        LocalDateTime createdAt = null;

        id = transaction.getId();
        amount = transaction.getAmount();
        payer = accountMapper.toResponseDTO( transaction.getPayer() );
        payee = accountMapper.toResponseDTO( transaction.getPayee() );
        createdAt = transaction.getCreatedAt();

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO( id, amount, payer, payee, createdAt );

        return transactionResponseDTO;
    }
}
