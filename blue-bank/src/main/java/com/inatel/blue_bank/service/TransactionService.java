package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Transaction;
import com.inatel.blue_bank.repository.TransactionRepository;
import com.inatel.blue_bank.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.inatel.blue_bank.repository.specs.TransactionSpecs.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;
    private final TransactionValidator validator;

    @Transactional // Ensures rollback in case anything goes wrong
    public Transaction save(Transaction transaction) {

        Account payer = transaction.getPayer();
        Account payee = transaction.getPayee();
        BigDecimal amount = transaction.getAmount();

        validator.validate(transaction);

        accountService.debit(payer, amount);
        accountService.credit(payee, amount);

        return repository.save(transaction);
    }

    public Optional<Transaction> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<Transaction> search(String payerAccountNumber,
                                    String payeeAccountNumber,
                                    String payerFullName,
                                    String payeeFullName,
                                    LocalDateTime createdAt,
                                    Integer page,
                                    Integer pageSize)
    {

        Specification<Transaction> specs = ((root, query, cb) -> cb.conjunction() );

        if (payerAccountNumber != null) {
            specs = specs.and(payerAccountNumberEqual(payerAccountNumber));
        }

        if (payeeAccountNumber != null) {
            specs = specs.and(payeeAccountNumberEqual(payeeAccountNumber));
        }

        if (payerFullName != null) {
            specs = specs.and(payerFullNameLike(payerFullName));
        }

        if (payeeFullName != null) {
            specs = specs.and(payeeFullNameLike(payeeFullName));
        }

        if(createdAt != null) {
            specs = specs.and(createdAtEqual(createdAt));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageRequest);
    }

    public void delete(Transaction transaction) {
        repository.delete(transaction);
    }
}
