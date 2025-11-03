package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.Account;
import com.inatel.blue_bank.model.AccountType;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.inatel.blue_bank.repository.specs.AccountSpecs.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public Account save(Account account) {
        // Validate
        return repository.save(account);
    }

    public Optional<Account> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Account> findByCustomerDoc(DocType docType, String docNumber) {
        return repository.findByCustomerDocTypeAndCustomerDocNumber(docType, docNumber);
    }

    public Page<Account> search(Long accountNumber,
                                 AccountType accountType,
                                 Integer branchCode,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt,
                                 Integer page,
                                 Integer pageSize)
    {

        Specification<Account> specs = ((root, query, cb) -> cb.conjunction() );

        if (accountNumber != null) {
            specs = specs.and(accountNumberEqual(accountNumber));
        }

        if(accountType != null) {
            specs = specs.and(accountTypeEqual(accountType));
        }

        if (branchCode != null) {
            specs = specs.and(branchCodeEqual(branchCode));
        }

        if (createdAt != null) {
            specs = specs.and(createdAtEqual(createdAt));
        }

        if(updatedAt != null) {
            specs = specs.and(updatedAtEqual(updatedAt));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageRequest);
    }

    public void update(Account account) {
        if(account.getId() == null) {
            throw new IllegalArgumentException("Account not found");
        }
        // Validate
        repository.save(account);
    }

    public void delete(Account account) {
        // Exception balance > 0.00
        repository.delete(account);
    }
}
