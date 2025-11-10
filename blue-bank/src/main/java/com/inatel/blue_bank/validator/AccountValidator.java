package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final AccountRepository repository;

    public void validate(Account account) {
        if (account.getId() == null) {
            // New account creation — check if account already exists
            boolean exists = repository
                    .existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                            account.getCustomer(),
                            account.getAccountNumber(),
                            account.getBranchCode());

            if (exists) {
                throw new DuplicateRegisterException("Account already exists");
            }
        } else {
            // Account update — ensure new data doesn't conflict with others
            boolean conflict = repository.existsDuplicateByCustomerOrAccountNumberAndBranchCodeOrId(
                    account.getCustomer(),
                    account.getAccountNumber(),
                    account.getBranchCode(),
                    account.getId()
            );

            if (conflict) {
                throw new DuplicateRegisterException("Another account already uses these details OR could not associate account with this customer");
            }
        }
    }
}
