package com.inatel.blue_bank.service;

import com.inatel.blue_bank.exception.DeniedOperationException;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import com.inatel.blue_bank.repository.AccountRepository;
import com.inatel.blue_bank.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.inatel.blue_bank.repository.specs.AccountSpecs.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final AccountValidator validator;

    public Account save(Account account) {
        validator.validate(account);
        return repository.save(account);
    }

    public Optional<Account> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Account> findByCustomerDoc(DocType docType, String docNumber) {
        return repository.findByCustomerDocTypeAndCustomerDocNumber(docType, docNumber);
    }

    public Page<Account> search(String accountNumber,
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
        validator.validate(account);
        repository.save(account);
    }

    public void delete(Account account) {
        if(hasBalance(account)){
            throw new DeniedOperationException(
                    "Account has balance greater than zero"
            );
        }
        Customer customer = account.getCustomer();
        customer.setAccount(null);
        repository.delete(account);
    }

    public boolean hasBalance(Account account) {
        return account.getBalance().compareTo(BigDecimal.ZERO) > 0;
    }
}
