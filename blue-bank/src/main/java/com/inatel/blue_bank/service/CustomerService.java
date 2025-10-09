package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    // CRUD
    private final CustomerRepository repository;

    public Customer save(Customer customer) {
        // TODO: VALIDATION
        return repository.save(customer);
    }

    public Optional<Customer> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Customer> findByDoc(DocType docType, String docNumber) {
        return repository.findByDoc(docType, docNumber);
    }

    public List<Customer> searchByExample(String name,
                                          String email,
                                          String nationality,
                                          String phone,
                                          LocalDate dob,
                                          String occupation)
    {
        Customer customer = new Customer();
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setNationality(nationality);
        customer.setPhone(phone);
        customer.setDob(dob);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "docNumber", "docType", "updatedAt")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Customer> customerExample = Example.of(customer, matcher);
        return repository.findAll(customerExample);
    }

    public void update(Customer customer) {
        if(customer.getId() == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        // TODO: VALIDATION
        repository.save(customer);
    }

    public void delete(Customer customer) {
        // TODO: EXCEPTION
//        if(hasAccount(customer)){
//            throw new DeniedOperationException(
//                    "Operation denied: Customer has an account"
//            )
//        }
        repository.delete(customer);
    }

    // TODO: hasAccount
}
