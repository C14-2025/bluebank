package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.repository.CustomerRepository;
import com.inatel.blue_bank.validation.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.inatel.blue_bank.repository.specs.CustomerSpecs.*;

@Service
@RequiredArgsConstructor
public class CustomerService {
    // CRUD
    private final CustomerRepository repository;
    private final CustomerValidator validator;
    private final CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        validator.validate(customer);
        return repository.save(customer);
    }

    public Optional<Customer> findById(UUID id) {
        return repository.findByIdWithoutAccount(id);
    }

    public Optional<Customer> findByDoc(DocType docType, String docNumber) {
        return repository.findByDocTypeAndDocNumber(docType, docNumber);
    }

    public Page<Customer> search(String fullName,
                                 LocalDate dob,
                                 String nationality,
                                 String occupation,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt,
                                 Integer page,
                                 Integer pageSize)
    {

        Specification<Customer> specs = ((root, query, cb) -> cb.conjunction() );

        if (fullName != null) {
            specs = specs.and(fullNameLike(fullName));
        }

        if(dob != null) {
            specs = specs.and(dobEqual(dob));
        }

        if (nationality != null) {
            specs = specs.and(nationalityLike(nationality));
        }

        if (occupation != null) {
            specs = specs.and(occupationLike(occupation));
        }

        if (createdAt != null) {
            specs = specs.and(createdAtEqual(createdAt));
        }

        if(updatedAt != null) {
            specs = specs.and(updatedAtEqual(updatedAt));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return customerRepository.findAllWithoutAccount(specs, pageRequest);
    }

    public void update(Customer customer) {
        if(customer.getId() == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        validator.validate(customer);
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
