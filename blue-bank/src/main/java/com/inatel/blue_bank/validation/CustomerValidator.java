package com.inatel.blue_bank.validation;

import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository repository;

    public void validate(Customer customer){
        if(customerExists(customer)){
            throw new DuplicateRegisterException("Customer already exists");
        }
    }

    private boolean customerExists(Customer customer){
        Optional<Customer> foundCustomer = repository.findByDocTypeAndDocNumberOrPhoneOrEmail(
                customer.getDocType(),
                customer.getDocNumber(),
                customer.getPhone(),
                customer.getEmail()
        );

        // If client doesn't provide an ID,
        // then they're requesting for
        // a new customer.
        if(customer.getId() == null){
            return foundCustomer.isPresent();
        }

        // If client is requesting an
        // update for a customer (with an ID),
        // and the new attributes aren't duplicating
        // any existing customer.
        if(foundCustomer.isEmpty()){
            return false;
        }

        // If client requests an update for
        // an existing customer (ID), with the
        // same previous attributes, ok.
        Customer existing = foundCustomer.get();

        return !customer.getId().equals(existing.getId());
    }
}
