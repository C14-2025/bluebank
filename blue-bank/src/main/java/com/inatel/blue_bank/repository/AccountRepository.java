package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("""
        SELECT COUNT(a) > 0 FROM Account a
        WHERE a.id <> :id
          AND ((a.accountNumber = :accountNumber AND a.branchCode = :branchCode)
           OR a.customer.id = :customerId)
    """)
    boolean existsDuplicateForUpdate(
            @Param("id") UUID id,
            @Param("accountNumber") Long accountNumber,
            @Param("branchCode") Integer branchCode,
            @Param("customerId") UUID customerId
    );

}
