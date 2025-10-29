package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByDocTypeAndDocNumber(DocType docType, String docNumber);

    // These query params were created,
    // in order to avoid Jpa relationship
    // error, as customer may not have an
    // account.
    @Query("""
    SELECT COUNT(c) > 0 FROM Customer c
    WHERE (c.docType = :docType AND c.docNumber = :docNumber)
       OR c.phone = :phone
       OR c.email = :email
    """)
    boolean existsDuplicate(
            @Param("docType") DocType docType,
            @Param("docNumber") String docNumber,
            @Param("phone") String phone,
            @Param("email") String email
    );

    @Query("""
        SELECT COUNT(c) > 0 FROM Customer c
        WHERE c.id <> :id
          AND ((c.docType = :docType AND c.docNumber = :docNumber)
           OR c.phone = :phone
           OR c.email = :email)
    """)
    boolean existsDuplicateForUpdate(
            @Param("id") UUID id,
            @Param("docType") DocType docType,
            @Param("docNumber") String docNumber,
            @Param("phone") String phone,
            @Param("email") String email
    );

    @Query("""
    SELECT new com.inatel.blue_bank.model.Customer(
        c.id, c.fullName, c.dob, c.nationality, c.phone,
        c.email, c.occupation, c.docType, c.docNumber,
        c.createdAt, c.updatedAt
    )
    FROM Customer c WHERE c.id = :id
    """)
    Optional<Customer> findByIdWithoutAccount(@Param("id") UUID id);

}
