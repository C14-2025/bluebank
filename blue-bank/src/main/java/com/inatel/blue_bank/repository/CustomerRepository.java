package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByDoc(DocType docType, String docNumber);
}
