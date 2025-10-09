package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByDocNumber(String docNumber);
    Optional<Customer> findByEmailAndDocNumber(String email, String docNumber);
}
