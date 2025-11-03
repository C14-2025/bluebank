package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.Account;
import com.inatel.blue_bank.model.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByCustomerDocTypeAndCustomerDocNumber(DocType docType, String docNumber);
}
