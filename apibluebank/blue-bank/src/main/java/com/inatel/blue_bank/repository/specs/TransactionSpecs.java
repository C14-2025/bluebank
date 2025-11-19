package com.inatel.blue_bank.repository.specs;

import com.inatel.blue_bank.model.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecs {

    public static Specification<Transaction> payerAccountNumberEqual(String payerAccountNumber) {
        return (root, query, cb) -> cb.equal(root.get("payerAccountNumber"), payerAccountNumber);
    }

    public static Specification<Transaction> payeeAccountNumberEqual(String payeeAccountNumber) {
        return (root, query, cb) -> cb.equal(root.get("payeeAccountNumber"), payeeAccountNumber);
    }

    public static Specification<Transaction> payerFullNameLike(String payerFullName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("payerFullName")), "%" + payerFullName.toUpperCase() + "%");
    }

    public static Specification<Transaction> payeeFullNameLike(String payeeFullName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("payeeFullName")), "%" + payeeFullName.toUpperCase() + "%");
    }

    public static Specification<Transaction> createdAtEqual(LocalDateTime createdAt) {
        return (root, query, cb) -> cb.equal(root.get("createdAt"), createdAt);
    }

}
