package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.CustomerNotFoundException;
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

        // This needs to be checked, because if mapper isn't able to find any customer,
        // it's going to set it to null.
        if (account.getCustomer() == null) {
            throw new CustomerNotFoundException("Provided customer does not exist");
        }

        if (account.getId() == null) {
            // New account creation — check if account already exists
            boolean exists = repository
                    .existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                            account.getCustomer(),
                            account.getAccountNumber(),
                            account.getBranchCode());

            if (exists) {
                throw new DuplicateRegisterException("Account already exists OR customer already has an account");
            }
        } else {
            // Account update — ensure new data doesn't conflict with others
            boolean conflict = repository.existsDuplicateForUpdate(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getBranchCode()
            );

            if (conflict) {
                throw new DuplicateRegisterException("Another account already uses these details");
            }
        }
    }
}
