package com.inatel.blue_bank.validation;

import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository repository;

    public void validate(Customer customer) {
        if (customer.getId() == null) {
            // New customer creation — check if someone already has any of these attributes
            boolean exists = repository.existsDuplicate(
                    customer.getDocType(),
                    customer.getDocNumber(),
                    customer.getPhone(),
                    customer.getEmail()
            );

            if (exists) {
                throw new DuplicateRegisterException("Customer already exists");
            }
        } else {
            // Customer update — ensure new data doesn't conflict with others
            boolean conflict = repository.existsDuplicateForUpdate(
                    customer.getId(),
                    customer.getDocType(),
                    customer.getDocNumber(),
                    customer.getPhone(),
                    customer.getEmail()
            );

            if (conflict) {
                throw new DuplicateRegisterException("Another customer already uses these details");
            }
        }
    }
}
