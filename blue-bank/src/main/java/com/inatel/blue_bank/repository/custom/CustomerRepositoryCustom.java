package com.inatel.blue_bank.repository.custom;

import com.inatel.blue_bank.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CustomerRepositoryCustom {
    Page<Customer> findAllWithoutAccount(Specification<Customer> spec, Pageable pageable);
}
