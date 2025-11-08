package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByCustomerDocTypeAndCustomerDocNumber(DocType docType, String docNumber);

    boolean existsByCustomer(Customer customer);

    // For SAVE use
    boolean existsDuplicateByCustomerOrAccountNumberAndBranchCode(
            Customer customer,
            Long accountNumber,
            Integer branchCode);

    // For UPDATE use
    boolean existsDuplicateByCustomerOrAccountNumberAndBranchCodeOrId(
            Customer customer,
            Long accountNumber,
            Integer branchCode,
            UUID id);
}
